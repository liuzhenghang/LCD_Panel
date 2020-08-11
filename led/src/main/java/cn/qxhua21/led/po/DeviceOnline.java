package cn.qxhua21.led.po;

import java.util.Date;

public class DeviceOnline {
    private int id;
    private String mac;
    private Date date;
    private String instructions;

    @Override
    public String toString() {
        return "DeviceOnline{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", date=" + date +
                ", instructions='" + instructions + '\'' +
                '}';
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
