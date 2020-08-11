package cn.qxhua21.led.mapper;

import cn.qxhua21.led.po.Device;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceMapper extends BaseMapper<Device> {
    @Update("update device set user_id = null where mac=#{mac}")
    int unbindDevice(String mac);
}
