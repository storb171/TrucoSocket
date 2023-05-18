public class Data {

    int command;
    String msg;

    public Data(int cmd, String msg) {
        this.command = cmd;
        this.msg = msg;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
