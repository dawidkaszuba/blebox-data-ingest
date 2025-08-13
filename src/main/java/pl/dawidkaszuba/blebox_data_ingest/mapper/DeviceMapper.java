package pl.dawidkaszuba.blebox_data_ingest.mapper;

import org.apache.ibatis.annotations.Param;
import pl.dawidkaszuba.blebox_data_ingest.model.Device;

import java.util.List;

public interface DeviceMapper {

    Device saveDevice(@Param("device") Device device,
                      @Param("changeUser") String changeUser);

    List<Device> findAllActiveDevicesWithSensors();
}
