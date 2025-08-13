package pl.dawidkaszuba.blebox_data_ingest.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class SensorWrapper {

    private final Map<String, SensorContainer> sensorData = new HashMap<>();

    @JsonAnySetter
    public void addSensorContainer(String name, SensorContainer container) {
        sensorData.put(name, container);
    }

    public Map<String, SensorContainer> getSensorData() {
        return sensorData;
    }
}
