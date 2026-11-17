package com.example.soa.automation;

import java.util.List;
import java.util.UUID;

public interface AutomationService {
    List<Automation> findAll();
    Automation findById(UUID id);
    Automation create(CreateAutomationRequest request);
    void delete(UUID id);
}
