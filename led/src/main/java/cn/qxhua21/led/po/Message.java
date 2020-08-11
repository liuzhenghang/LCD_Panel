package cn.qxhua21.led.po;

public class Message {
    private String mac;
    private Object msg;
    private String type;
    private String toWhere;
    private int qos;
    private String topic;


    public static Message online(String mac,String ip){
        Message message=new Message();
        message.setMac(mac);
        message.setMsg("online,"+ip);
        message.setType("line");
        message.setToWhere("user");
        return message;
    }
    public static Message offline(String mac){
        Message message=new Message();
        message.setMac(mac);
        message.setMsg("offline");
        message.setType("line");
        message.setToWhere("user");
        return message;
    }
    public static Message toDevice(String mac,Object msg,int qos){
        Message message=new Message();
        message.setMac(mac);
        message.setMsg(msg);
        message.setType("instruction");
        message.setToWhere("device");
        message.setQos(qos);
        return message;
    }
    public static Message toUserRefresh(String mac, Object msg){
        Message message=new Message();
        message.setMac(mac);
        message.setMsg(msg);
        message.setType("refresh");
        message.setToWhere("user");
        return message;
    }
    public static Message SocketConnect(){
        Message message=new Message();
        message.setMsg("success");
        message.setType("socket");
        message.setToWhere("user");
        return message;
    }
    public static Message SocketKickOut(){
        Message message=new Message();
        message.setMsg("KickOut");
        message.setType("socket");
        message.setToWhere("user");
        return message;
    }
    public static Message SocketTokenError(){
        Message message=new Message();
        message.setMsg("TokenError");
        message.setType("socket");
        message.setToWhere("user");
        return message;
    }


    public String getToWhere() {
        return toWhere;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mac='" + mac + '\'' +
                ", msg=" + msg +
                ", type='" + type + '\'' +
                ", toWhere='" + toWhere + '\'' +
                ", qos=" + qos +
                ", topic='" + topic + '\'' +
                '}';
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }



    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
