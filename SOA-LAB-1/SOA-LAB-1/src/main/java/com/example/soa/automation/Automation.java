package com.example.soa.automation;

import java.util.UUID;

public record Automation(UUID id, String name, String action, boolean enabled) { }
