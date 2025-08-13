package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metric {
    private Integer value;
    private String unit;
}
