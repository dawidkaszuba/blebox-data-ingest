package pl.dawidkaszuba.blebox_data_ingest.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceConfig {
    private String name;
    private String ip;
    private List<DeviceSensor> sensors;
}