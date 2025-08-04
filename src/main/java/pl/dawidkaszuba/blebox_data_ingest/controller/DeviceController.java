package pl.dawidkaszuba.blebox_data_ingest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dawidkaszuba.blebox_data_ingest.model.AddDeviceRequest;
import pl.dawidkaszuba.blebox_data_ingest.model.BleboxResponse;
import pl.dawidkaszuba.blebox_data_ingest.service.DeviceService;

@Slf4j
@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<BleboxResponse> addDevice(@RequestBody AddDeviceRequest request) {
        BleboxResponse response = deviceService.addDevice(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
