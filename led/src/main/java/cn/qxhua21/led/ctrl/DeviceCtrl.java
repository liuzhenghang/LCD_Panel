package cn.qxhua21.led.ctrl;


import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/device")
public class DeviceCtrl {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private HttpServletRequest request;


    @PostMapping("/message")
    @ResponseBody
    public Object DeviceMessage(String mac){
        Token token=new Token(Integer.parseInt(request.getHeader("id")),
                request.getHeader("token"));
        return deviceService.getDeviceMessage(token,mac);
    }
//    用户为设备下发指令
    @PostMapping("/upText")
    public Object instructions(String text,String mac){
        int userId= Integer.parseInt(request.getHeader("id"));
        Token token=new Token(userId,request.getHeader("token"));
        return deviceService.sendInstructions(token,text,mac);
    }

}
