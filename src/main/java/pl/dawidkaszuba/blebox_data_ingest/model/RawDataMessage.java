package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
public class RawDataMessage {
    private String device;
    private String source;
    private OffsetDateTime received;
    private String schemaVersion;
    private Map<String, Metric> metrics;
}
