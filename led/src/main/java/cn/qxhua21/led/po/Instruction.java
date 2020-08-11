package cn.qxhua21.led.po;

public class Instruction {
    private String type;
    private Object value;

    public static Instruction GET(){
        Instruction instruction=new Instruction();
        instruction.setType("get");
        return instruction;
    }
    public static Instruction SET(Object data){
        Instruction instruction=new Instruction();
        instruction.setType("set");
        instruction.setValue(data);
        return instruction;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
