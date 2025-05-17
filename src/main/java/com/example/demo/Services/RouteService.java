package com.example.demo.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class RouteService {
    private final RestTemplate rest;
    private final ObjectMapper mapper;
    private final String URL;

    public RouteService(
            @Value("${route.service.url}") String url
    ) {
        this.rest   = new RestTemplate();
        this.mapper = new ObjectMapper();
        this.URL    = url;
    }

    /**
     * Returns the travel duration in minutes between two points,
     * given a desired departure time.
     */
    public double getTravelTimeMinutes(
            double srcLat, double srcLon,
            double dstLat, double dstLon,
            ZonedDateTime departureTime
    ) {
        // Build request payload as a JsonNode
        ObjectNode root = mapper.createObjectNode();
        var locations = root.putArray("locations");

        // source
        var src = locations.addObject();
        src.put("lat", srcLat);
        src.put("lon", srcLon);
        var dt = src.putObject("date_time");
        dt.put("type", 1);
        dt.put("value", departureTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // destination (no date_time needed)
        var dst = locations.addObject();
        dst.put("lat", dstLat);
        dst.put("lon", dstLon);

        // costing & options
        root.put("costing", "auto");
        var costingOpts = root.putObject("costing_options")
                .putObject("auto");
        costingOpts.put("shortest", true);
        root.put("units", "kilometers");
        root.put("language", "en-US");
        root.put("format", "json");

        // POST it
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JsonNode> req = new HttpEntity<>(root, headers);

        ResponseEntity<JsonNode> resp = rest.exchange(
                URL, HttpMethod.POST, req, JsonNode.class
        );

        JsonNode summary = Objects.requireNonNull(resp.getBody())
                .path("trip")
                .path("summary");

        // extract only the time attribute
        return summary.path("time").asDouble();
    }
}
