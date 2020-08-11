package cn.qxhua21.led.websocket;

import cn.qxhua21.led.dao.UserDao;
import cn.qxhua21.led.po.Message;
import cn.qxhua21.led.po.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/websocket/{sid}/{token}",encoders = {ServerEncoder.class})
@Component
@Service
public class WebSocketUserServer {

    private static UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao){
        WebSocketUserServer.userDao=userDao;
    }
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineUser = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    //发送消息
    public static boolean sendMessage(Session session, Object message) throws IOException {
        if(session != null){
            synchronized (session) {
                try {
                    session.getBasicRemote().sendObject(message);
                } catch (EncodeException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    //给指定用户发送信息
    public static boolean sendInfo(String userId, Object message){
        Session session = sessionPools.get(userId);
        try {
             return (sendMessage(session, message));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static void closeUser(String userId){
        Session session = sessionPools.get(userId);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session,
                       @PathParam(value = "sid") String userId,
                       @PathParam(value = "token") String token){
        System.out.println(userId);
        System.out.println(token);
        Token token1=new Token(Integer.parseInt(userId),token);
        System.out.println(token1);

        if (userDao.catToken(token1)){
            if (sessionPools.get(userId)!=null){
                sendInfo(userId,Message.SocketKickOut());
                closeUser(userId);
            }
            sessionPools.put(userId, session);
            addOnlineCount();
            System.out.println(userId + "加入webSocket！当前人数为" + onlineUser);
            try {
                sendMessage(session, Message.SocketConnect());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                sessionPools.put(userId, session);
                addOnlineCount();
                sendMessage(session, Message.SocketTokenError());
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "sid") String userId,
                        @PathParam(value = "token") String token){
        sessionPools.remove(userId);
        subOnlineCount();
        System.out.println(userId + "断开webSocket连接！当前人数为" + onlineUser);
    }

    //收到客户端信息
    @OnMessage
    public void onMessage(String message) {
        message = "客户端：" + message + ",已收到";
        System.out.println(message);
        for (Session session: sessionPools.values()) {
            try {
                sendMessage(session, message);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    public static void addOnlineCount(){
        onlineUser.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineUser.decrementAndGet();
    }

}