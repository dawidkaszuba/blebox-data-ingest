package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Device {
    private Long id;
    private String macAddress;
    private String name;
    private String ip;
    private List<SensorReading> sensors;
}
