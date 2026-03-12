package lk.ijse.gdse72.sensorservice.client;

import lk.ijse.gdse72.sensorservice.DTO.ExternalAuthRequest;
import lk.ijse.gdse72.sensorservice.DTO.ExternalAuthResponse;
import lk.ijse.gdse72.sensorservice.DTO.TelemetryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "external-iot-service", url = "${sensor.external-api-url}")
public interface ExternalIotClient {

    @PostMapping("/auth/login")
    ExternalAuthResponse login(@RequestBody ExternalAuthRequest request);

    @GetMapping("/devices/telemetry/{deviceId}")
    TelemetryDTO getLatestTelemetry(
            @RequestHeader("Authorization") String token,
            @PathVariable String deviceId
    );
}