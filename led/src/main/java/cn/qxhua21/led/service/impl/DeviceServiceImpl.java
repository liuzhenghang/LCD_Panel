package cn.qxhua21.led.service.impl;

import cn.qxhua21.led.config.CodeMsg;
import cn.qxhua21.led.config.Result;
import cn.qxhua21.led.config.atText;
import cn.qxhua21.led.dao.DeviceDao;
import cn.qxhua21.led.dao.UserDao;
import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;
import cn.qxhua21.led.service.DeviceService;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private UserDao userDao;

    @Override
    public Object getDeviceMessage(Token token,String mac) {
        if (userDao.catToken(token)){
            return Result.success(deviceDao.getDeviceMessageByMac(mac));
        }
        return Result.error(CodeMsg.SESSSON_ERROR);
    }
    @Override
    public Object sendInstructions(Token token,String text,String mac) {
        if (userDao.catToken(token)){
            Device device=new Device();
            device.setMac(mac);
            JSONObject json=new JSONObject();
            json.put("text",text);
            if (deviceDao.putInstructionsForDevice(device,json)){
                return Result.success(null);
            }
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        return Result.error(CodeMsg.SESSSON_ERROR);
    }


    @Override
    public Object getDevicesByUserId(Token token) {
        if (userDao.catToken(token)){
            return Result.success(deviceDao.getDeviceByUserId(token.getId()));
        }
        return Result.error(CodeMsg.SESSSON_ERROR);
    }

    @Override
    public Object bindDevice(Token token, Device device) {
        if (userDao.catToken(token)){
            return deviceDao.bindDevice(device);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object unbindDevice(Token token, Device device, User user) {
        if (userDao.catToken(token)){
            if (userDao.testIdLogin(user)){
                if (deviceDao.unbindDevice(device)){

                    return Result.success(null);
                }
                return Result.error(CodeMsg.SERVER_ERROR);
            }
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        return Result.error(CodeMsg.SESSSON_ERROR);
    }
}
