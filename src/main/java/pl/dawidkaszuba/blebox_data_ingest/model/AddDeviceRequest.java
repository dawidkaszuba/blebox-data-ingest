package pl.dawidkaszuba.blebox_data_ingest.model;

import lombok.Getter;

@Getter
public class AddDeviceRequest {
    private String ip;
    private String name;
}
