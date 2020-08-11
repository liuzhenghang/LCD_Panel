package cn.qxhua21.led.dao;

import cn.qxhua21.led.po.DeviceState;

public interface DeviceStateDao {
    void updateDeviceState(DeviceState deviceState,boolean update);
    DeviceState getDeviceState(String mac);
}
