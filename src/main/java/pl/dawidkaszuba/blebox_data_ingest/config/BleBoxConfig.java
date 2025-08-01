package pl.dawidkaszuba.blebox_data_ingest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "blebox")
@PropertySource(value = "classpath:devices.yml", factory = YamlPropertySourceFactory.class)
public class BleBoxConfig {

    private List<DeviceConfig> devices = new ArrayList<>();

    public List<DeviceConfig> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceConfig> devices) {
        this.devices = devices;
    }
}
