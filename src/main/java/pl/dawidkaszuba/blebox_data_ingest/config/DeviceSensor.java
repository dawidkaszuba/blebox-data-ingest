package pl.dawidkaszuba.blebox_data_ingest.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceSensor {
    private String path;
    private String typeField;
    private String valueField;
}
