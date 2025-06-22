package com.example.demo.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchedRequestDTO {
    private String userId;
    private String requestId;
}
