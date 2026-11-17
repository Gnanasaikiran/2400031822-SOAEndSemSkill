package com.example.soa.user;

import java.util.UUID;

public record User(UUID id, String name, String email) { }
