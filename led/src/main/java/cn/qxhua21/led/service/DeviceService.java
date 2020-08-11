package cn.qxhua21.led.service;

import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;

public interface DeviceService {
    Object getDeviceMessage(Token token,String mac);
    Object sendInstructions(Token token, String text,String mac);
    Object getDevicesByUserId(Token token);
    Object bindDevice(Token token, Device device);
    Object unbindDevice(Token token, Device device, User user);
}
