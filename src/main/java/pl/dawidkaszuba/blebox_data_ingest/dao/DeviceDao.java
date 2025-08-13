package pl.dawidkaszuba.blebox_data_ingest.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dawidkaszuba.blebox_data_ingest.mapper.DeviceMapper;
import pl.dawidkaszuba.blebox_data_ingest.mapper.SensorMapper;
import pl.dawidkaszuba.blebox_data_ingest.model.AddDeviceRequest;
import pl.dawidkaszuba.blebox_data_ingest.model.Device;
import pl.dawidkaszuba.blebox_data_ingest.model.SensorConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DeviceDao {

    private static final String CHANGE_USER = "blebox-app";

    private final DeviceMapper deviceMapper;
    private final SensorMapper sensorMapper;

    public DeviceDao(DeviceMapper deviceMapper, SensorMapper sensorMapper) {
        this.deviceMapper = deviceMapper;
        this.sensorMapper = sensorMapper;
    }

    @Transactional
    public void saveDeviceWithSensors(String macAddress, AddDeviceRequest request) {
        Device deviceToSave = new Device();
        deviceToSave.setMacAddress(macAddress);
        deviceToSave.setIp(request.getIp());
        deviceToSave.setName(request.getName());
        Device device = deviceMapper.saveDevice(deviceToSave, CHANGE_USER);

        List<SensorConfig> sensorsFromRequest = request.getSensors();
        List<SensorConfig> sensorsFromDb = sensorMapper.findAllActiveSensorsByDeviceId(device.getId());

        Set<SensorConfig> requestSensorSet = new HashSet<>(sensorsFromRequest);
        Set<SensorConfig> existingSensorSet = new HashSet<>(sensorsFromDb);

        requestSensorSet.removeAll(existingSensorSet);

        requestSensorSet
                .forEach(sensor -> sensorMapper.saveSensor(device.getId(), sensor, CHANGE_USER));

        sensorsFromDb.forEach(sensorConfig -> {
            if (!request.getSensors().contains(sensorConfig)) {
                sensorMapper.deleteSensor(sensorConfig.getId());
            }
        });
    }
}
