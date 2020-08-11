package cn.qxhua21.led.ctrl;


import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;
import cn.qxhua21.led.service.DeviceService;
import cn.qxhua21.led.service.UserService;
import cn.qxhua21.led.websocket.WebSocketUserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
@ResponseBody
public class UserCtrl {
    @Autowired
    HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;


    @GetMapping("/send")
    public ModelAndView send(){
        ModelAndView modelAndView=new ModelAndView("/send");
        return modelAndView;
    }
    @PostMapping("/post")
    public void post(String id,String message){
        WebSocketUserServer.sendInfo(id,message);
    }

    @PostMapping("/login")
    public Object login(String mail,String password){
        User user=new User();
        user.setMail(mail);
        user.setPassword(password);
        return userService.Login(user);
    }

    @PostMapping("/register")
    public Object register(String mail,String username,String password){
        User user=new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setUsername(username);
        return userService.register(user);
    }

    //获取用户的设备
    @PostMapping("/devices")
    public Object getDevices(){
        Token token=new Token(Integer.parseInt(request.getHeader("id")),
                request.getHeader("token"));
        return deviceService.getDevicesByUserId(token);
    }
    @PostMapping("/token")
    public Object catToken(){
        Token token=new Token(Integer.parseInt(request.getHeader("id")),
                request.getHeader("token"));
        return deviceService.getDevicesByUserId(token);
    }

    //绑定设备
    @PostMapping("/addDevices")
    public Object addDevices(String mac,String name){
        Token token=new Token(Integer.parseInt(request.getHeader("id")),
                request.getHeader("token"));
        Device device=new Device();
        device.setMac(mac);
        device.setName(name);
        device.setUserId(token.getId());
        return deviceService.bindDevice(token,device);
    }
    //解绑设备
    @PostMapping("/unbind")
    public Object unbindDevices(String mac,String pass){
        Token token=new Token(Integer.parseInt(request.getHeader("id")),
                request.getHeader("token"));
        Device device=new Device();
        device.setMac(mac);
        User user=new User();
        user.setId(token.getId());
        user.setPassword(pass);
        return deviceService.unbindDevice(token,device,user);
    }

}
