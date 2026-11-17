package com.example.soa.user;

import com.example.soa.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryUserService implements UserService {
    private final ConcurrentMap<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public List<User> findAll() {
        return users.values().stream().sorted(Comparator.comparing(User::name)).toList();
    }

    @Override
    public User findById(UUID id) {
        User user = users.get(id);
        if (user == null) throw new ResourceNotFoundException("User not found: " + id);
        return user;
    }

    @Override
    public User create(CreateUserRequest request) {
        User user = new User(UUID.randomUUID(), request.name(), request.email());
        users.put(user.id(), user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        if (users.remove(id) == null) throw new ResourceNotFoundException("User not found: " + id);
    }
}
