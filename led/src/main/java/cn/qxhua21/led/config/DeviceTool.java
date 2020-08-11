package cn.qxhua21.led.config;

import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.DeviceState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceTool {
    public static Device static2device(Device device, DeviceState deviceState){
        if (deviceState==null){
            device.setOnline(false);
            return device;
        }
        device.setDate(deviceState.getDate());
        device.setIp(deviceState.getIp());
        device.setOnline(deviceState.isOnline());
        device.setVersion(deviceState.getVersion());
        device.setText(deviceState.getText());
        return device;
    }
}
