import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Player implements Runnable {

    String name;
    Socket socket;
    String player;
    ObjectInputStream serverIn;
    ObjectOutputStream serverOut;

    public Player(Socket socket, String player) throws IOException {
        this.socket = socket;
        this.player = player;
        serverIn = new ObjectInputStream(socket.getInputStream());
        serverOut = new ObjectOutputStream(socket.getOutputStream());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void sendMsg(String msg) {
        try {
            serverOut.writeObject(msg);
            serverOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String receiveMsg() {
        String msg = "";
        try {
            msg = serverIn.readObject().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public void run() {
        Object msgIn = null;
        try {
            msgIn = serverIn.readObject();
            setName(msgIn.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
