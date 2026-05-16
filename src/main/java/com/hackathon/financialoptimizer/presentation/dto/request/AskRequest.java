package com.hackathon.financialoptimizer.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AskRequest(
        @NotBlank(message = "question is required")
        @Size(min = 5, max = 500, message = "question must be between 5 and 500 characters")
        String question
) {}
