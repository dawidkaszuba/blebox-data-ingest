package pl.dawidkaszuba.blebox_data_ingest.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface NotificationService {

    void sendRawData(JsonNode normalizedData);
}

