package pl.dawidkaszuba.blebox_data_ingest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "metrics")
@PropertySource(value = "classpath:metrics-units.yml", factory = YamlPropertySourceFactory.class)
public class MetricsConfig {


    private Map<String, MetricDefinition> definitions;

    public MetricDefinition getDefinition(String name) {
        return definitions.getOrDefault(name, new MetricDefinition(1.0, ""));
    }

    public Map<String, MetricDefinition> getDefinitions() { return definitions; }
    public void setDefinitions(Map<String, MetricDefinition> definitions) { this.definitions = definitions; }

    public static class MetricDefinition {
        private double scale;
        private String unit;

        public MetricDefinition() {}
        public MetricDefinition(double scale, String unit) {
            this.scale = scale;
            this.unit = unit;
        }

        public double getScale() { return scale; }
        public void setScale(double scale) { this.scale = scale; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
    }
}
