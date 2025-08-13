package pl.dawidkaszuba.blebox_data_ingest.mapper;

import org.apache.ibatis.annotations.Param;
import pl.dawidkaszuba.blebox_data_ingest.model.SensorConfig;

import java.util.List;

public interface SensorMapper {
    void saveSensor(@Param("deviceId") Long deviceId,
                    @Param("sensor") SensorConfig sensor,
                    @Param("changeUser") String changeUser);

    List<SensorConfig> findAllActiveSensorsByDeviceId(@Param("deviceId") Long deviceId);

    void deleteSensor(@Param("id") Long id);
}
