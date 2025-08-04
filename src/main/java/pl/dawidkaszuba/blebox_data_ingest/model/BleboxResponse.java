package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BleboxResponse {
    private String deviceId;
    private BleboxDeviceStatus status;
    private String message;
    private String errorCode;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
}
