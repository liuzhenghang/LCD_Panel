package cn.qxhua21.led.po;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("device")
public class Device {
    private int id;
    private String name;
    private String mac;
    private String ip;
    private int userId;
    private boolean online;
    private String text;
    private String version;
    private String date;

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", userId=" + userId +
                ", online=" + online +
                ", text='" + text + '\'' +
                ", version='" + version + '\'' +
                ", date=" + date +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
