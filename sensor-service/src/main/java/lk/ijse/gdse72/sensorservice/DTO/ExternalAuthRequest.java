package lk.ijse.gdse72.sensorservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExternalAuthRequest {
    private String username;
    private String password;
}
