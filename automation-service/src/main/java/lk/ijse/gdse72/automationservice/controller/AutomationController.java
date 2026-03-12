package lk.ijse.gdse72.automationservice.controller;

import lk.ijse.gdse72.automationservice.client.ZoneClient;
import lk.ijse.gdse72.automationservice.entity.ActionLog;
import lk.ijse.gdse72.automationservice.repository.ActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    @Autowired
    private ActionLogRepository actionLogRepository;

    @Autowired
    private ZoneClient zoneClient;

    @PostMapping("/process")
    public ResponseEntity<String> processTelemetry(@RequestBody Map<String, Object> telemetryData) {
        try {
            String zoneId = telemetryData.get("zoneId").toString();
            Map<String, Object> valueMap = (Map<String, Object>) telemetryData.get("value");
            Double currentTemp = Double.valueOf(valueMap.get("temperature").toString());

            // Ask Zone Service for limits
            Map<String, Object> zoneDetails = zoneClient.getZoneById(zoneId);
            if (zoneDetails != null && zoneDetails.containsKey("minTemp") && zoneDetails.containsKey("maxTemp")) {
                Double minTemp = Double.valueOf(zoneDetails.get("minTemp").toString());
                Double maxTemp = Double.valueOf(zoneDetails.get("maxTemp").toString());

                // Rule Engine
                if (currentTemp > maxTemp) {
                    logAction(zoneId, currentTemp, maxTemp, "TURN_FAN_ON");
                } else if (currentTemp < minTemp) {
                    logAction(zoneId, currentTemp, minTemp, "TURN_HEATER_ON");
                }
            } else {
                System.out.println("No Zone details found for zoneId: " + zoneId);
            }
            return ResponseEntity.ok("Processed successfully");
        } catch (Exception e) {
            System.err.println("Error processing telemetry: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error processing telemetry");
        }
    }

    private void logAction(String zoneId, Double currentTemp, Double thresholdTemp, String action) {
        ActionLog log = new ActionLog();
        log.setZoneId(zoneId);
        log.setCurrentTemp(currentTemp);
        log.setThresholdTemp(thresholdTemp);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        actionLogRepository.save(log);
        System.out.println("AUTOMATION RULE TRIGGERED: " + action + " for Zone " + zoneId);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ActionLog>> getLogs() {
        return ResponseEntity.ok(actionLogRepository.findAll());
    }
}
