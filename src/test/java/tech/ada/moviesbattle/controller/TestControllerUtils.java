package tech.ada.moviesbattle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class TestControllerUtils {

    public static String getBasicAuthHeaderValue(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
