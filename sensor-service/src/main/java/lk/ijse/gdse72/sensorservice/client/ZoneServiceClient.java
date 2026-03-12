package lk.ijse.gdse72.sensorservice.client;

import lk.ijse.gdse72.sensorservice.DTO.ZoneDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "zone-service")
public interface ZoneServiceClient {

    @GetMapping("/api/v1/zones")
    List<ZoneDTO> getAllZones();
}