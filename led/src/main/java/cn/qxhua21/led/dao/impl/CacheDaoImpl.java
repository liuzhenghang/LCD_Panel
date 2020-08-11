package cn.qxhua21.led.dao.impl;

import cn.qxhua21.led.config.SHA;
import cn.qxhua21.led.dao.CacheDao;
import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.mapper.DeviceMapper;
import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.DeviceState;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CacheDaoImpl implements CacheDao {
    @Value("${spring.redis.devices-user}")
    private String deviceTmp;

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private RedisUtils redis;
    @Override
    public void refreshDeviceCache() {
        if (!redis.hasKey(deviceTmp)){
            List<Device> devices=deviceMapper.selectList(null);
            Map<String,Object> map=new HashMap<>();
            for (Device device:devices){
                map.put(device.getMac(),device);
            }
            redis.hmset(deviceTmp,map,3600);
        }
    }

    @Override
    public void updateDevice(Device device) {

    }

    @Override
    public void updateDeviceState(DeviceState deviceState) {
        //更新数据库
        UpdateWrapper<Device> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("mac",deviceState.getMac());
        System.out.println(deviceState);
        refreshDeviceCache();
        Device device= (Device) redis.hget(deviceTmp,deviceState.getMac());
        if (deviceState.getIp()!=null){
            if (!deviceState.getIp().equals("")){
                device.setIp(deviceState.getIp());
            }
            device.setDate(SHA.getTime());
            deviceMapper.update(device,updateWrapper);
            //移除缓存
            cleanDeviceCache();
        }else {
            device.setDate(SHA.getTime());
            device.setText(deviceState.getText());
            device.setVersion(deviceState.getVersion());
            deviceMapper.update(device,updateWrapper);
            //移除缓存
            cleanDeviceCache();
        }
        System.out.println(device);
    }

    @Override
    public void cleanDeviceCache() {
        redis.del(deviceTmp);
    }
}
