package pl.dawidkaszuba.blebox_data_ingest.model;

import java.util.List;

public class SensorContainer {
    private List<SensorReading> sensors;

    public List<SensorReading> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorReading> sensors) {
        this.sensors = sensors;
    }
}
