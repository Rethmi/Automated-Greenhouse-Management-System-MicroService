package lk.ijse.gdse72.sensorservice.service;

import lk.ijse.gdse72.sensorservice.DTO.*;
import lk.ijse.gdse72.sensorservice.client.AutomationClient;
import lk.ijse.gdse72.sensorservice.client.ExternalIotClient;
import lk.ijse.gdse72.sensorservice.client.ZoneServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemetryService {

    @Autowired
    private ExternalIotClient externalIotClient;

    @Autowired
    private AutomationClient automationClient;

    @Autowired
    private ZoneServiceClient zoneServiceClient;

    private String cachedToken;
    private TelemetryDTO latestReading;

    @Scheduled(fixedRateString = "${sensor.fetch-rate}")
    public void fetchAndPushTask() {
        try {

            if (cachedToken == null) {
                ExternalAuthResponse auth = externalIotClient.login(new ExternalAuthRequest("Anjana", "1234"));
                cachedToken = "Bearer " + auth.getAccessToken();
            }


            List<ZoneDTO> zones = zoneServiceClient.getAllZones();

            if (zones == null || zones.isEmpty()) {
                System.out.println("No zones found to fetch telemetry.");
                return;
            }

            for (ZoneDTO zone : zones) {
                if (zone.getDeviceId() != null && !zone.getDeviceId().isEmpty()) {

                    TelemetryDTO data = externalIotClient.getLatestTelemetry(cachedToken, zone.getDeviceId());

                    if (data != null) {

                        data.setZoneId(zone.getId().toString());
                        this.latestReading = data;

                        try {
                            automationClient.pushData(data);
                            System.out.println("Telemetry pushed for Zone: " + zone.getName());
                        } catch (Exception ex) {
                            System.err.println("Pusher Error for zone " + zone.getName() + ": Automation Service unreachable.");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Critical Telemetry Fetching Error: " + e.getMessage());
            cachedToken = null;
        }
    }

    public TelemetryDTO getLatestReading() {
        return latestReading;
    }
}