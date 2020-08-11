package cn.qxhua21.led.dao.impl;

import cn.qxhua21.led.config.CodeMsg;
import cn.qxhua21.led.config.DeviceTool;
import cn.qxhua21.led.config.Result;
import cn.qxhua21.led.dao.CacheDao;
import cn.qxhua21.led.dao.DeviceDao;
import cn.qxhua21.led.dao.DeviceStateDao;
import cn.qxhua21.led.mapper.DeviceMapper;
import cn.qxhua21.led.mqtt.MqttSender;
import cn.qxhua21.led.po.*;
import cn.qxhua21.led.thread.AsyncService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DeviceDaoImpl implements DeviceDao {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private DeviceStateDao deviceStateDao;
    @Autowired
    private MqttSender mqttSender;
    @Autowired
    private CacheDao cacheDao;
    @Override
    public boolean addDevice(Device device) {
        if (deviceMapper.insert(device)>0){
            return true;
        }
        return false;
    }

    @Override
    public Object bindDevice(Device device) {
        QueryWrapper<Device> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getMac,device.getMac());
        Device device1=deviceMapper.selectOne(queryWrapper);
        if (device1==null){
            return Result.error(CodeMsg.MAC_NOTFOUND);
        }
        if (device1.getUserId()>0){
            return Result.error(CodeMsg.DEVICE_HAS_BE_BIND);
        }
        device.setId(device1.getId());
        if (deviceMapper.updateById(device)>0){
            cacheDao.cleanDeviceCache();
            return Result.success(null);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public boolean unbindDevice(Device device) {
        if (deviceMapper.unbindDevice(device.getMac())>0){
            cacheDao.cleanDeviceCache();
            return true;
        }
        return false;
    }

    @Override
    public void deviceOnline(String mac, String ip) {
        Message message=Message.online(mac,ip);
        asyncService.executeAsync(message);

    }

    @Override
    public void deviceOffline(String mac) {
        Message message=Message.offline(mac);
        asyncService.executeAsync(message);
    }

    @Override
    public boolean getDeviceMessageByMac(String mac) {
        JSONObject json=new JSONObject();
        json.put("type","get");
        mqttSender.sendToMqtt(mac,2, JSON.toJSONString(json));
        return true;
    }

    @Override
    public List<Device> getDeviceByUserId(int id) {
        Device device=new Device();
        device.setUserId(id);
        QueryWrapper<Device> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getUserId,id);
        List<Device> devices=deviceMapper.selectList(queryWrapper);
        for (int i=0;i<devices.size();i++){
            String mac=devices.get(i).getMac();
            devices.set(i, DeviceTool.static2device(devices.get(i),deviceStateDao.getDeviceState(mac)));
        }
        return devices;
    }

    @Override
    public List<DeviceOnline> getDeviceOnline() {
        return null;
    }

    @Override
    public boolean putInstructionsForDevice(Device device,Object data) {
        String mac=device.getMac();
        Instruction instruction=Instruction.SET(data);
        mqttSender.sendToMqtt(mac,2,JSON.toJSONString(instruction));
        return true;
    }



}
