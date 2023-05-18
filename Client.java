import com.google.gson.Gson;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static Socket socket = null;
    static List<Card> hand = new ArrayList<>();

    static ObjectInputStream serverIn;
    static ObjectOutputStream serverOut;

    public static void sendMsg(String msg) {
        try {
            if (serverOut == null) {
                serverOut = new ObjectOutputStream(socket.getOutputStream());
            }
            serverOut.writeObject(msg);
            serverOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String receiveMsg() {
        String strMsg = "";
        try {
            if (serverIn == null) {
                serverIn = new ObjectInputStream(socket.getInputStream());
            }
            strMsg = serverIn.readObject().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMsg;
    }

    public static void connect() {
        boolean success = false;
        while (!success) {
            try {
                String ip = "localhost";
                int port = 5000;
                socket = new Socket(ip, port);
                success = true;
                System.out.println("Conectando ao servidor");
            } catch (IOException e) {
                System.out.println("Servidor ainda não esta aberto, tentando novamente...");
            }
        }
    }

    public static void main(String args[]) throws IOException {
        Gson gson = new Gson();
        Scanner read = new Scanner(System.in);
        System.out.println("Digite seu nome: ");
        String name = read.next();
        connect();
        sendMsg(name);
        loopPrincipal: while (true) {
            String ms = receiveMsg();
            Data d = gson.fromJson(ms, Data.class);
            switch (d.getCommand()) {
                case 0:
                    hand.add(gson.fromJson(d.getMsg(), Card.class));
                    break;
                case 1:
                    System.out.println(d.getMsg());
                    break;
                case 2:
                    System.out.println("Opções: ");
                    int i = 0;
                    for (Card c : hand) {
                        System.out.print("[" + i + "] - " + c.getValue() + c.getNipe() + "\n");
                        i++;
                    }
                    System.out.println("[3] Peça Truco!\nEscolha uma opção:");
                    int option = read.nextInt();
                    while (option < 0 || (option > hand.size() - 1 && option < 3) || option > 3) {
                        System.out.println("[ERRO] Opção invalida!");
                        option = read.nextInt();
                    }
                    if (option == 3) {
                        sendMsg(gson.toJson(new Card("", "TRUCO")));
                        System.out.println("Agora escolha uma carta!");
                        option = read.nextInt();
                        while (option < 0 || option > hand.size() - 1) {
                            System.out.println("[ERRO] Opção invalida!");
                            option = read.nextInt();
                        }
                    }
                    sendMsg(gson.toJson(hand.get(option)));
                    hand.remove(option);
                    break;
                case 3:
                    hand = new ArrayList<>();
                    break;
                case 4:
                    System.out.println("\nVai aceitar o truco? [0] - Não | [1] - Sim");
                    int op = read.nextInt();
                    while (op != 0 && op != 1) {
                        System.out.println("Opção invalida! [0] ou [1]");
                        op = read.nextInt();
                    }
                    sendMsg(gson.toJson(op));
                    System.out.println("Aguarde seu parceiro decidir...");
                    break;
                case 5:
                    System.out.println(d.getMsg());
                    System.out.println("Desconectando...");
                    break loopPrincipal;
            }
        }
    }
}
