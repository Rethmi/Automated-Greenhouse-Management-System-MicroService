package lk.ijse.gdse72.sensorservice.controller;

import lk.ijse.gdse72.sensorservice.DTO.TelemetryDTO;
import lk.ijse.gdse72.sensorservice.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

    @Autowired
    private TelemetryService telemetryService;


    @GetMapping("/latest")
    public ResponseEntity<TelemetryDTO> getLatest() {
        return ResponseEntity.ok(telemetryService.getLatestReading());
    }
}
