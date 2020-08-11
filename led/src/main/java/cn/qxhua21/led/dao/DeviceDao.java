package cn.qxhua21.led.dao;

import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.DeviceOnline;
import cn.qxhua21.led.po.DeviceState;

import java.util.List;

public interface DeviceDao {
//    添加设备
    boolean addDevice(Device device);
//    绑定设备
    Object bindDevice(Device device);
    boolean unbindDevice(Device device);
    //设备上下线提醒
    void deviceOnline(String mac, String ip);
    void deviceOffline(String mac);


//    通过MAC获取设备信息（简单信息）
    boolean getDeviceMessageByMac(String mac);
//    获取用户下的设备
    List<Device> getDeviceByUserId(int id);

//    获取在线列表
    List<DeviceOnline> getDeviceOnline();
//    给设备发送指令
    boolean putInstructionsForDevice(Device device,Object value);

}
