package tech.ada.moviesbattle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.moviesbattle.dto.AnswerDto;
import tech.ada.moviesbattle.dto.MatchResponseDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MatchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void startMatch() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/new")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieOne").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieTwo").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        MatchResponseDto dto = new ObjectMapper().readValue(responseBody, MatchResponseDto.class);

        assertEquals(dto.getWrongAnswers(),0);
        assertEquals(dto.getRightAnswers(),0);
        assertEquals(dto.getId(), 3);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/stop")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                )
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    void stopMatch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/new")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieOne").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieTwo").exists())
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/stop")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieOne").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieTwo").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        MatchResponseDto dto = new ObjectMapper().readValue(responseBody, MatchResponseDto.class);

        assertEquals(dto.getWrongAnswers(),0);
        assertEquals(dto.getRightAnswers(),0);
        assertEquals(dto.getId(), 5);
        assertFalse(dto.isActive());
    }

    @Test
    void answerRound() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/new")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieOne").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieTwo").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        MatchResponseDto dto = new ObjectMapper().readValue(responseBody, MatchResponseDto.class);

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/round")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                        .content(TestControllerUtils.asJsonString(new AnswerDto(dto.getMovieOne().getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieOne").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieTwo").exists())
                .andReturn();

        String responseBody2 = result2.getResponse().getContentAsString();

        assertNotEquals(responseBody, responseBody2);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/match/stop")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                )
                .andExpect(status().isCreated()).andReturn();
    }
}