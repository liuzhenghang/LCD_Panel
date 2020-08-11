package cn.qxhua21.led.ctrl;


import cn.qxhua21.led.websocket.WebSocketUserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SocketController {

    @Autowired
    private WebSocketUserServer webSocketUserServer;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav=new ModelAndView("/index");
        return mav;
    }

    @GetMapping("/webSocket")
    public ModelAndView socket() {
        ModelAndView mav=new ModelAndView("/webSocket");
//        mav.addObject("userId", userId);
        return mav;
    }


}