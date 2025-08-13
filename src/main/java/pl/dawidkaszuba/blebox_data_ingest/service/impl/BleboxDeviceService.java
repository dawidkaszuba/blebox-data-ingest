package pl.dawidkaszuba.blebox_data_ingest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.blebox_data_ingest.dao.DeviceDao;
import pl.dawidkaszuba.blebox_data_ingest.api.BleBoxClient;
import pl.dawidkaszuba.blebox_data_ingest.model.AddDeviceRequest;
import pl.dawidkaszuba.blebox_data_ingest.model.BleboxDeviceStatus;
import pl.dawidkaszuba.blebox_data_ingest.model.BleboxResponse;
import pl.dawidkaszuba.blebox_data_ingest.service.DeviceService;

import java.time.LocalDateTime;

@Slf4j
@Service
public class BleboxDeviceService implements DeviceService {

    private final BleBoxClient bleBoxClient;
    private final DeviceDao deviceDao;

    public BleboxDeviceService(BleBoxClient bleBoxClient, DeviceDao deviceDao) {
        this.bleBoxClient = bleBoxClient;
        this.deviceDao = deviceDao;
    }

    @Override
    public BleboxResponse addDevice(AddDeviceRequest request) {
        try {

            String uid = bleBoxClient.fetchMacAddressFromBlebox(request.getIp());
            saveDevice(uid, request);

            return BleboxResponse.builder()
                    .deviceId(uid)
                    .status(BleboxDeviceStatus.DEVICE_UPSERTED)
                    .message("Device added successfully")
                    .httpStatus(HttpStatus.CREATED)
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            return BleboxResponse.builder()
                    .status(BleboxDeviceStatus.ERROR)
                    .message("Unexpected error: " + e.getMessage())
                    .errorCode("BLEBOX_UNKNOWN_ERROR")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }



    private void saveDevice(String mac, AddDeviceRequest request) {
        deviceDao.saveDeviceWithSensors(mac, request);
        log.info("Dodano urządzenie BleBox: ip: {} mac: ({})", request.getIp(), mac);
    }
}
