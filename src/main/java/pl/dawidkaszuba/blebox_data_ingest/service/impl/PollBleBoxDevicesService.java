package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.blebox_data_ingest.api.BleBoxClient;
import pl.dawidkaszuba.blebox_data_ingest.mapper.DeviceMapper;
import pl.dawidkaszuba.blebox_data_ingest.model.*;
import pl.dawidkaszuba.blebox_data_ingest.service.NotificationService;
import pl.dawidkaszuba.blebox_data_ingest.service.MessageMapper;
import pl.dawidkaszuba.blebox_data_ingest.service.PollDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class PollBleBoxDevicesService implements PollDataService {

    private final DeviceMapper deviceMapper;
    private final MessageMapper messageMapper;
    private final NotificationService notificationService;
    private final BleBoxClient bleBoxClient;

    public PollBleBoxDevicesService(DeviceMapper deviceMapper,
                                    MessageMapper messageMapper,
                                    NotificationService notificationService, BleBoxClient bleBoxClient) {
        this.deviceMapper = deviceMapper;
        this.messageMapper = messageMapper;
        this.notificationService = notificationService;
        this.bleBoxClient = bleBoxClient;
    }

    @Override
    public void pollDevices() {
        List<Device> devices = deviceMapper.findAllActiveDevicesWithSensors();
        devices.forEach(this::pollDevice);
    }

    private void pollDevice(Device device) {

        SensorWrapper wrapper = bleBoxClient.pollDevice(device);

        List<SensorReading> allSensors = attachUnitsToSensors(device.getSensors(), wrapper.getSensorData());

        RawDataMessage message = messageMapper.map(device, allSensors);

        notificationService.sendRawData(message);
    }

    private List<SensorReading> attachUnitsToSensors(List<SensorReading> sensorReadings, Map<String, SensorContainer> sensorConfigs) {

        Map<String, String> sensorsTypesWithUnits = sensorReadings.stream()
                .collect(Collectors.toMap(
                        SensorReading::getType,
                        SensorReading::getUnit
                ));

        List<SensorReading> allSensors = new ArrayList<>();
        sensorConfigs.values().forEach(container -> {
            container.getSensors().forEach(sensor -> sensor.setUnit(sensorsTypesWithUnits.get(sensor.getType())));
            allSensors.addAll(container.getSensors());

        });
        return allSensors;
    }
}
