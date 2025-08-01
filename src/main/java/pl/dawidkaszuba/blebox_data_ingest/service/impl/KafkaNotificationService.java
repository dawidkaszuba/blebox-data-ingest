package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.blebox_data_ingest.exception.SerializationException;
import pl.dawidkaszuba.blebox_data_ingest.service.NotificationService;

@Service
public class KafkaNotificationService implements NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String rawDataTopicName;
    private final ObjectMapper objectMapper;

    public KafkaNotificationService(KafkaTemplate<String, String> kafkaTemplate,
                                    @Value("${data-ingest.kafka.raw-data-topic.name}") String rawDataTopicName,
                                    ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.rawDataTopicName = rawDataTopicName;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendRawData(JsonNode normalizedData) {
        try {
            String json = objectMapper.writeValueAsString(normalizedData);
            kafkaTemplate.send(rawDataTopicName, json);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serialization of JSON message has occurred ", e);
        }
    }
}
