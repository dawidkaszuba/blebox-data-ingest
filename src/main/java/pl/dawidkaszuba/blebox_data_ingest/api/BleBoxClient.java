package pl.dawidkaszuba.blebox_data_ingest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.dawidkaszuba.blebox_data_ingest.exception.PollDeviceException;
import pl.dawidkaszuba.blebox_data_ingest.model.Device;
import pl.dawidkaszuba.blebox_data_ingest.model.SensorWrapper;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class BleBoxClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public BleBoxClient(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public String fetchMacAddressFromBlebox(String ip) {
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

    public SensorWrapper pollDevice(Device device) {
        try {
            String response = webClient.get()
                    .uri("http://{ip}/state", device.getIp())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readValue(response, SensorWrapper.class);

        } catch (Exception e) {
            throw new PollDeviceException("Error during polling devices values", e);
        }
    }

}
