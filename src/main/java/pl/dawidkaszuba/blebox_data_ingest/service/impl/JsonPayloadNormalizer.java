package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.blebox_data_ingest.config.DeviceConfig;
import pl.dawidkaszuba.blebox_data_ingest.config.MetricsConfig;
import pl.dawidkaszuba.blebox_data_ingest.service.PayloadNormalizer;

import java.time.OffsetDateTime;

@Slf4j
@Service
public class JsonPayloadNormalizer implements PayloadNormalizer {

    private static final String SOURCE_PREFIX = "BleBox";
    private static final String VALUE = "value";

    private final ObjectMapper objectMapper;
    private final MetricsConfig metricsConfig;

    public JsonPayloadNormalizer(ObjectMapper objectMapper, MetricsConfig metricsConfig) {
        this.objectMapper = objectMapper;
        this.metricsConfig = metricsConfig;
    }

    @Override
    public ObjectNode normalize(DeviceConfig device, Object payload) {
        try {

            String objectPayload = objectMapper.writeValueAsString(payload);

            JsonNode rawData = objectMapper.readTree(objectPayload);

            ObjectNode normalizedData = objectMapper.createObjectNode();
            normalizedData.put("device", device.getName() + "@" + device.getIp());
            normalizedData.put("source", SOURCE_PREFIX + "@" + device.getIp());
            normalizedData.put("received", OffsetDateTime.now().toString());
            normalizedData.put("schemaVersion", "1.0");

            ObjectNode metrics = objectMapper.createObjectNode();

            device.getSensors().forEach(sensorConfig -> {
                JsonNode sensorsArray = getNodeByPath(rawData, sensorConfig.getPath());
                if (sensorsArray.isArray()) {
                    for (JsonNode sensor : sensorsArray) {
                        String type = sensor.path(sensorConfig.getTypeField()).asText("unknown");
                        double rawValue = sensor.path(sensorConfig.getValueField()).asDouble(Double.NaN);

                        ObjectNode metric = objectMapper.createObjectNode();

                        MetricsConfig.MetricDefinition def = metricsConfig.getDefinition(type);
                        double scaledValue = rawValue * def.getScale();
                        String unit = def.getUnit();

                        metric.put(VALUE, scaledValue);
                        metric.put("unit", unit);

                        metrics.set(type, metric);
                    }
                } else {
                    log.warn("Brak sekcji {} w odpowiedzi BleBox z urządzenia {}",
                            sensorConfig.getPath(), device.getIp());
                }
            });

            normalizedData.set("metrics", metrics);
            return normalizedData;
        } catch (Exception e) {
            log.error("Błąd parsowania JSON: {}", e.getMessage());
            return null;
        }
    }

    private JsonNode getNodeByPath(JsonNode root, String path) {
        JsonNode node = root;
        for (String key : path.split("\\.")) {
            node = node.path(key);
        }
        return node;
    }

}
