package com.example.soa.automation;

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

@WebMvcTest(AutomationController.class)
@Import(ApiExceptionHandler.class)
class AutomationControllerTest {
    @Autowired MockMvc mvc;
    @MockBean AutomationService service;

    @Test
    void listReturnsAutomations() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        when(service.findAll()).thenReturn(List.of(new Automation(id, "Daily report", "email", true)));

        mvc.perform(get("/api/automations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Daily report"))
                .andExpect(jsonPath("$[0].enabled").value(true));
        verify(service).findAll();
    }

    @Test
    void getByIdUsesUuidRouteAndReturnsAutomation() throws Exception {
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");
        when(service.findById(id)).thenReturn(new Automation(id, "Sync", "sync", false));

        mvc.perform(get("/api/automations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.action").value("sync"));
        verify(service).findById(id);
    }

    @Test
    void createReturns201AndPassesValidatedBodyToService() throws Exception {
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");
        CreateAutomationRequest request = new CreateAutomationRequest("Daily report", "email", true);
        when(service.create(request)).thenReturn(new Automation(id, request.name(), request.action(), request.enabled()));

        mvc.perform(post("/api/automations").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Daily report\",\"action\":\"email\",\"enabled\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Daily report"));
        verify(service).create(request);
    }

    @Test
    void createRejectsBlankRequiredFields() throws Exception {
        mvc.perform(post("/api/automations").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \",\"action\":\"\",\"enabled\":true}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void missingAutomationMapsTo404() throws Exception {
        UUID id = UUID.fromString("44444444-4444-4444-4444-444444444444");
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Automation not found: " + id));

        mvc.perform(get("/api/automations/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Automation not found: " + id));
    }

    @Test
    void deleteReturns204AndDelegates() throws Exception {
        UUID id = UUID.fromString("55555555-5555-5555-5555-555555555555");
        mvc.perform(delete("/api/automations/{id}", id)).andExpect(status().isNoContent());
        verify(service).delete(id);
    }
}
