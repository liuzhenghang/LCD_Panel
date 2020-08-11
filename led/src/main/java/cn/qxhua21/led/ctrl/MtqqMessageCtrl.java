package cn.qxhua21.led.ctrl;

import cn.qxhua21.led.config.Result;
import cn.qxhua21.led.dao.DeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MtqqMessageCtrl {
    @Autowired
    private DeviceDao deviceDao;
    @RequestMapping("/webHook")
    @ResponseBody
    public Object putMessage(@RequestBody Map<String,String> param){
//        String action = param.get(MtqqMessageCtrl.ACTION);
//        String username = param.get(MtqqMessageCtrl.USERNAME);
        String action=param.get("action");
        switch (action){
            case "client_connected":
                System.out.print("设备连接:");
//                System.out.println(param);
                System.out.print("ip="+param.get("ipaddress"));
                System.out.println("MAC="+param.get("clientid"));
                deviceDao.deviceOnline(param.get("clientid"),param.get("ipaddress"));
                break;
            case "client_disconnected":
                System.out.print("设备断开:");
//                System.out.println(param);
                System.out.println("MAC="+param.get("clientid"));
                deviceDao.deviceOffline(param.get("clientid"));
                break;
        }

        return Result.success(null);
    }
}
