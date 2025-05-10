package com.example.demo.Services;

import com.example.demo.Enums.GenderType;
import com.example.demo.Exceptions.GenderFetchException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GenderService {

    private final RestTemplate rest;
    private final ObjectMapper mapper;
    //private static final String URL_TEMPLATE = "http://localhost:8080/api/profiles/gender/{userId}";
    private static final String URL_TEMPLATE = "https://5d2b-197-48-41-75.ngrok-free.app/api/profiles/gender/{userId}";

    public GenderService() {
        this.rest = new RestTemplate();
        this.mapper = new ObjectMapper();
    }

    /**
     * Fetches the gender for a given user ID from the profile service.
     */
    public GenderType getGender(String userId) {
        try {
            ResponseEntity<String> response = rest.getForEntity(URL_TEMPLATE, String.class, userId);
            String json = response.getBody();
            JsonNode root = mapper.readTree(json);

            String genderValue;
            if (root.isTextual()) {
                genderValue = root.textValue();
            } else if (root.has("gender")) {
                genderValue = root.get("gender").asText();
            } else {
                throw new GenderFetchException("Gender field not found in profile response for user " + userId);
            }

            return GenderType.valueOf(genderValue.toLowerCase());
        } catch (JsonProcessingException | RestClientException | IllegalArgumentException e) {
            throw new GenderFetchException("Failed to fetch gender for user " + userId, e);
        }
    }
}
