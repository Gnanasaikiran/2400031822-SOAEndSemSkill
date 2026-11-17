package com.example.soa.automation;

import jakarta.validation.constraints.NotBlank;

public record CreateAutomationRequest(
        @NotBlank String name,
        @NotBlank String action,
        boolean enabled) { }
