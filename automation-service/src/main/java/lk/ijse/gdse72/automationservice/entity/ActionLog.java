package lk.ijse.gdse72.automationservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zoneId;
    private Double currentTemp;
    private Double thresholdTemp;
    private String action; // e.g. "TURN_FAN_ON", "TURN_HEATER_ON"
    private LocalDateTime timestamp;
}
