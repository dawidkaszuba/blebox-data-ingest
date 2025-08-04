package pl.dawidkaszuba.blebox_data_ingest.service;

import pl.dawidkaszuba.blebox_data_ingest.model.AddDeviceRequest;
import pl.dawidkaszuba.blebox_data_ingest.model.BleboxResponse;

public interface DeviceService {
    BleboxResponse addDevice(AddDeviceRequest request);
}
