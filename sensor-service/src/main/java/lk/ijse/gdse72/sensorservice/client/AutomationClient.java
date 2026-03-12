package lk.ijse.gdse72.sensorservice.client;

import lk.ijse.gdse72.sensorservice.DTO.TelemetryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "automation-and-controller-service")
public interface AutomationClient {

    @PostMapping("/api/v1/automation/process")
    void pushData(@RequestBody TelemetryDTO data);
}