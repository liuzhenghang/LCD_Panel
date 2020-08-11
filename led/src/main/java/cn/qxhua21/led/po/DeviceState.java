package cn.qxhua21.led.po;


import cn.qxhua21.led.config.SHA;


//指令代码
public class DeviceState {
    private String mac;
    private String ip;
    private boolean online;
    private String text;
    private String version;
    private String date;

    public DeviceState() {
        this.date = SHA.getTime();
    }

    @Override
    public String toString() {
        return "DeviceState{" +
                "mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", online=" + online +
                ", text='" + text + '\'' +
                ", version='" + version + '\'' +
                ", date=" + date +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
