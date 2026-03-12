package lk.ijse.gdse72.zoneservice.controller;

import lk.ijse.gdse72.zoneservice.client.IoTClient;
import lk.ijse.gdse72.zoneservice.entity.Zone;
import lk.ijse.gdse72.zoneservice.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private IoTClient ioTClient;

    @PostMapping
    public ResponseEntity<Zone> createZone(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Zone zone) {
        if (zone.getMinTemp() != null && zone.getMaxTemp() != null && zone.getMinTemp() >= zone.getMaxTemp()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minTemp must be strictly less than maxTemp");
        }

        if (zone.getId() == null || zone.getId().isEmpty()) {
            zone.setId(UUID.randomUUID().toString());
        }

        try {
            // Register device in the IoT API
            Map<String, String> payload = Map.of(
                    "name", zone.getName() + "-Sensor",
                    "zoneId", zone.getId()
            );
            Map<String, Object> response = ioTClient.registerDevice(token, payload);
            
            if (response != null && response.containsKey("deviceId")) {
                zone.setDeviceId(response.get("deviceId").toString());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve deviceId from IoT Provider");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error communicating with external IoT service: " + e.getMessage());
        }

        Zone saved = zoneRepository.save(zone);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZone(@PathVariable String id) {
        return zoneRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Zone>> getAllZones() {
        return ResponseEntity.ok(zoneRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateThresholds(@PathVariable String id, @RequestBody Zone updatedDetails) {
        return zoneRepository.findById(id).map(zone -> {
            if (updatedDetails.getMinTemp() != null && updatedDetails.getMaxTemp() != null 
                    && updatedDetails.getMinTemp() >= updatedDetails.getMaxTemp()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minTemp must be strictly less than maxTemp");
            }
            if (updatedDetails.getMinTemp() != null) zone.setMinTemp(updatedDetails.getMinTemp());
            if (updatedDetails.getMaxTemp() != null) zone.setMaxTemp(updatedDetails.getMaxTemp());
            if (updatedDetails.getName() != null) zone.setName(updatedDetails.getName());
            return ResponseEntity.ok(zoneRepository.save(zone));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable String id) {
        if (zoneRepository.existsById(id)) {
            zoneRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
