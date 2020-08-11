package cn.qxhua21.led.dao;

import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.DeviceState;

public interface CacheDao {
    //检查并刷新设备列表的缓存
    void refreshDeviceCache();
    //    更新设备基本信息
    void updateDevice(Device device);
    //    更新设备状态到数据库
    void updateDeviceState(DeviceState deviceState);
    void cleanDeviceCache();
}
