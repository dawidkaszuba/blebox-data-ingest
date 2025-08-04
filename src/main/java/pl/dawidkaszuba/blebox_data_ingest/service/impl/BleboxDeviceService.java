package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.dawidkaszuba.blebox_data_ingest.mapper.DeviceMapper;
import pl.dawidkaszuba.blebox_data_ingest.model.AddDeviceRequest;
import pl.dawidkaszuba.blebox_data_ingest.model.BleboxDeviceStatus;
import pl.dawidkaszuba.blebox_data_ingest.model.BleboxResponse;
import pl.dawidkaszuba.blebox_data_ingest.service.DeviceService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class BleboxDeviceService implements DeviceService {

    private static final String CHANGE_USER = "blebox-app";
    private final WebClient webClient;
    private final DeviceMapper deviceMapper;

    public BleboxDeviceService(WebClient webClient, DeviceMapper deviceMapper) {
        this.webClient = webClient;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public BleboxResponse addDevice(AddDeviceRequest request) {
        try {

            String uid = fetchMacAddressFromBlebox(request.getIp());

            saveDevice(uid, request.getName(), request.getIp());

            return BleboxResponse.builder()
                    .deviceId(uid)
                    .status(BleboxDeviceStatus.DEVICE_UPSERTED)
                    .message("Device added successfully")
                    .httpStatus(HttpStatus.CREATED)
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            return BleboxResponse.builder()
                    .status(BleboxDeviceStatus.ERROR)
                    .message("Unexpected error: " + e.getMessage())
                    .errorCode("BLEBOX_UNKNOWN_ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    private String fetchMacAddressFromBlebox(String ip) {
        String url = "http://" + ip + "/api/device/network";

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3))
                .map(body -> (String) body.get("mac"))
                .block();

    }

    private void saveDevice(String mac, String name, String ip) {
        deviceMapper.saveDevice(mac, name, ip, CHANGE_USER);
        log.info("Dodano urządzenie BleBox: ip: {} mac: ({})", ip, mac);
    }
}
