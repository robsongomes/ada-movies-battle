package tech.ada.moviesbattle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import tech.ada.moviesbattle.repository.MovieRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class RankingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MovieRepository movieRepository;

    @Test
    void rankingListEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/ranking")
                        .header(HttpHeaders.AUTHORIZATION, TestControllerUtils.getBasicAuthHeaderValue("player1", "123456"))
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

}