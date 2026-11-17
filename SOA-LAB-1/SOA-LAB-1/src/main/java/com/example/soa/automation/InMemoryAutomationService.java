package com.example.soa.automation;

import com.example.soa.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryAutomationService implements AutomationService {
    private final ConcurrentMap<UUID, Automation> automations = new ConcurrentHashMap<>();

    @Override
    public List<Automation> findAll() {
        return automations.values().stream().sorted(Comparator.comparing(Automation::name)).toList();
    }

    @Override
    public Automation findById(UUID id) {
        Automation automation = automations.get(id);
        if (automation == null) throw new ResourceNotFoundException("Automation not found: " + id);
        return automation;
    }

    @Override
    public Automation create(CreateAutomationRequest request) {
        Automation automation = new Automation(UUID.randomUUID(), request.name(), request.action(), request.enabled());
        automations.put(automation.id(), automation);
        return automation;
    }

    @Override
    public void delete(UUID id) {
        if (automations.remove(id) == null) throw new ResourceNotFoundException("Automation not found: " + id);
    }
}
