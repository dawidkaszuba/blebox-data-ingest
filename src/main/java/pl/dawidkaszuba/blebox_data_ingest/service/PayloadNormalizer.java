package pl.dawidkaszuba.blebox_data_ingest.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pl.dawidkaszuba.blebox_data_ingest.config.DeviceConfig;

public interface PayloadNormalizer {
    ObjectNode normalize(DeviceConfig device, Object payload);
}
