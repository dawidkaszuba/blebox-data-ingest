package pl.dawidkaszuba.blebox_data_ingest.mapper;

import org.apache.ibatis.annotations.Param;

public interface DeviceMapper {
    void saveDevice(@Param("mac") String mac,
                    @Param("name") String deviceName,
                    @Param("ip") String ip,
                    @Param("changeUser") String changeUser);
}
