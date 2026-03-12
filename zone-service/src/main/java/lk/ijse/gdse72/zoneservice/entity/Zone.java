package lk.ijse.gdse72.zoneservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zone {
    @Id
    private String id;
    private String name;
    private Double minTemp;
    private Double maxTemp;
    private String deviceId; // Returned from the IoT API
}
