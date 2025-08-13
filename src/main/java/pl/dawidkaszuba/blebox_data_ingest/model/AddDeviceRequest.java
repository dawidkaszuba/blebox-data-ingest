package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.Getter;

import java.util.List;

@Getter
public class AddDeviceRequest {
    private String ip;
    private String name;
    private List<SensorConfig> sensors;
}
