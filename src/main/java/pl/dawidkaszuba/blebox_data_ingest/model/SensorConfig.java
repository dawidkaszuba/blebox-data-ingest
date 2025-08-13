package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(exclude = "id")
public class SensorConfig {
    private Long id;
    private String name;
    private String unit;
}
