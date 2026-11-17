package com.example.soa.user;

import com.example.soa.common.ApiExceptionHandler;
import com.example.soa.common.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(ApiExceptionHandler.class)
class UserControllerTest {
    @Autowired MockMvc mvc;
    @MockBean UserService service;

    @Test
    void listReturnsUsers() throws Exception {
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        when(service.findAll()).thenReturn(List.of(new User(id, "Asha Rao", "asha@example.com")));

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].email").value("asha@example.com"));
        verify(service).findAll();
    }

    @Test
    void getByIdReturnsUser() throws Exception {
        UUID id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        when(service.findById(id)).thenReturn(new User(id, "Asha Rao", "asha@example.com"));

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asha Rao"));
        verify(service).findById(id);
    }

    @Test
    void createReturns201AndPassesBodyToService() throws Exception {
        UUID id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
        CreateUserRequest request = new CreateUserRequest("Asha Rao", "asha@example.com");
        when(service.create(request)).thenReturn(new User(id, request.name(), request.email()));

        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Asha Rao\",\"email\":\"asha@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value("asha@example.com"));
        verify(service).create(request);
    }

    @Test
    void createRejectsInvalidEmail() throws Exception {
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Asha Rao\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void missingUserMapsTo404() throws Exception {
        UUID id = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("User not found: " + id));

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found: " + id));
    }

    @Test
    void deleteReturns204AndDelegates() throws Exception {
        UUID id = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");
        mvc.perform(delete("/api/users/{id}", id)).andExpect(status().isNoContent());
        verify(service).delete(id);
    }
}
