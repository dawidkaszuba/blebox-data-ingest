package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.dawidkaszuba.blebox_data_ingest.config.BleBoxConfig;
import pl.dawidkaszuba.blebox_data_ingest.config.DeviceConfig;
import pl.dawidkaszuba.blebox_data_ingest.service.NotificationService;
import pl.dawidkaszuba.blebox_data_ingest.service.PayloadNormalizer;
import pl.dawidkaszuba.blebox_data_ingest.service.PollDataService;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Slf4j
@Service
public class PollBleboxDevicesService implements PollDataService {
    
    private final BleBoxConfig bleBoxConfig;
    private final PayloadNormalizer payloadNormalizer;
    private final WebClient webClient;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public PollBleboxDevicesService(BleBoxConfig bleBoxConfig, PayloadNormalizer payloadNormalizer, WebClient webClient, NotificationService notificationService, ObjectMapper objectMapper) {
        this.bleBoxConfig = bleBoxConfig;
        this.payloadNormalizer = payloadNormalizer;
        this.webClient = webClient;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void pollDevices() {
        bleBoxConfig.getDevices().forEach(this::pollDevice);
    }

    private void pollDevice(DeviceConfig device) {
        webClient.get()
                .uri("http://{ip}/state", device.getIp())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Błąd HTTP z BleBox {}: {}", device.getIp(), body))
                                .then(Mono.error(new RuntimeException("HTTP error from BleBox"))))
                .bodyToMono(JsonNode.class)
                .timeout(Duration.ofSeconds(5))
                .map(response -> {
                    return payloadNormalizer.normalize(device, response);
                })
                .doOnNext(notificationService::sendRawData)
                .doOnError(e -> log.error("Błąd połączenia z BleBox {}: {}", device.getIp(), e.getMessage()))
                .subscribe();
    }
}
