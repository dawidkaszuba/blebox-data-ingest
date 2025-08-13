package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.blebox_data_ingest.model.Device;
import pl.dawidkaszuba.blebox_data_ingest.model.Metric;
import pl.dawidkaszuba.blebox_data_ingest.model.RawDataMessage;
import pl.dawidkaszuba.blebox_data_ingest.model.SensorReading;
import pl.dawidkaszuba.blebox_data_ingest.service.MessageMapper;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RawMessageMapper implements MessageMapper {

    private static final String SOURCE_PREFIX = "BleBox";


    @Override
    public RawDataMessage map(Device device, List<SensorReading> sensorList) {

        RawDataMessage rawDataMessage = new RawDataMessage();
        String deviceData = device.getName() + "@" + device.getIp();
        String source = SOURCE_PREFIX + "@" + device.getIp();
        String schemaVersion = "1.0";
        Map<String, Metric> metrics = new HashMap<>();

        sensorList.forEach(sensor -> {
            Metric metric = new Metric();
            metric.setUnit(sensor.getUnit());
            metric.setValue(sensor.getValue());
            metrics.put(sensor.getType(), metric);
        });

        rawDataMessage.setDevice(deviceData);
        rawDataMessage.setSource(source);
        rawDataMessage.setSchemaVersion(schemaVersion);
        rawDataMessage.setReceived(OffsetDateTime.now());
        rawDataMessage.setMetrics(metrics);
        return rawDataMessage;
    }


}
