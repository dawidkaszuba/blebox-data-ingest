package pl.dawidkaszuba.blebox_data_ingest.service;

import pl.dawidkaszuba.blebox_data_ingest.model.RawDataMessage;

public interface NotificationService {

    void sendRawData(RawDataMessage rawDataMessage);
}

