package com.example.soa.user;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAll();
    User findById(UUID id);
    User create(CreateUserRequest request);
    void delete(UUID id);
}
