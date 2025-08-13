package pl.dawidkaszuba.blebox_data_ingest.service;

import pl.dawidkaszuba.blebox_data_ingest.model.Device;
import pl.dawidkaszuba.blebox_data_ingest.model.RawDataMessage;
import pl.dawidkaszuba.blebox_data_ingest.model.SensorReading;

import java.util.List;

public interface MessageMapper {
    RawDataMessage map(Device device, List<SensorReading> sensorList);
}
