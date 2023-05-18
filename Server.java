import com.google.gson.Gson;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<Player> players;

    public static void msgToAll(String msg) {
        Gson gson = new Gson();
        for (Player p : players) {
            p.sendMsg(gson.toJson(new Data(1, msg)));
        }
    }

    public static void main(String args[]) {
        Gson gson = new Gson();
        players = new ArrayList<Player>();
        int port = 5000;

        try {
            System.out.println("[SERVIDOR] Aguardando por jogadores...");
            ServerSocket socketServer = new ServerSocket(port);
            for (int i = 1; i < 5; i++) {
                Socket socketAccept = socketServer.accept();
                System.out.println("[SERVIDOR] Conectado ao cliente: " + socketAccept.getInetAddress().getHostAddress() + ":" + socketAccept.getPort());
                Player p = new Player(socketAccept, i % 2 == 0 ? "Time 1" : "Time 2");
                new Thread(p).start();
                p.sendMsg(gson.toJson(new Data(1, "Você é o Jogador " + i)));
                players.add(p);
                System.out.println("[SERVIDOR] " + p.getPlayer() + " foi conectado!\n");
            }

            Deck.shuffleCards();
            
            Card manilha = new Card();
            Deck deck = new Deck();
            manilha = deck.getManilha();
            System.out.println(manilha.valor);

            int roundValue = 1;
            int playerOnePoints = 0;
            int playerTwoPoints = 0;

            String[] round = new String[3];

            String winner = "";

            boolean isTruco = false;

            while (playerOnePoints < 12 && playerTwoPoints < 12) {
                for (Player p : players) {
                    p.sendMsg(gson.toJson(new Data(3, gson.toJson(new Card()))));
                    List<Card> cards = Deck.addCards();
                    for (Card c : cards) {
                        Data h = new Data(0, gson.toJson(c));
                        p.sendMsg(gson.toJson(h));
                    }
                }
                msgToAll("");
                msgToAll("Pontuação:");
                msgToAll("[Time 1]: " + playerOnePoints + "\n[Time 2]: " + playerTwoPoints);
                msgToAll("\nA manilha é " + manilha.getValue() + manilha.getNipe() + "!\n");

                whileLoop: for (int i = 0; i < 3; i++) {
                    for (int turn = 0; turn < 4; turn++) {
                        Player p = players.get(turn);
                        p.sendMsg(gson.toJson(new Data(2, gson.toJson(new Card()))));
                        Card card = gson.fromJson(p.receiveMsg(), Card.class);

                        if (card.getValue().equals("TRUCO")) {
                            card = gson.fromJson(p.receiveMsg(), Card.class);
                            if (!isTruco) {
                                isTruco = true;
                                msgToAll("\n[" + p.getPlayer() + "] " + p.getName() + " pediu truco!");
                                int acc = 0;
                                for (Player pp : players) {
                                    if (!pp.getPlayer().equals(pp.getPlayer())) {
                                        pp.sendMsg(gson.toJson(new Data(4, gson.toJson(new Card()))));
                                        acc = acc + Integer.parseInt(pp.receiveMsg());
                                    }
                                }

                                if (acc != 2) {
                                    winner = p.getName();
                                    msgToAll("Quem está levando: " + winner);
                                    break whileLoop;
                                } else {
                                    roundValue = roundValue + 2;
                                    msgToAll("Rodada vale " + roundValue + " pontos.");
                                }
                            } else {
                                p.sendMsg(gson.toJson(new Data(1, "Rodada ja está trucada!")));
                            }
                        }

                        msgToAll(p.getName() + " jogou " + card.getValue() + card.getNipe() + "!\n");
                        manilha = Card.valuesOfCards(manilha, card);
                        if (manilha.getValue().equals(card.getValue())
                                && manilha.getNipe().equals(card.getNipe())) {
                            round[i] = p.getPlayer();
                        }
                    }

                    if (i == 0) {
                        msgToAll(round[0] + "º round foi ganho com " + manilha.getValue() + manilha.getNipe());
                    } else if (i == 1) {
                        if (round[0].equals(round[1])) {
                            msgToAll("[Rodada " + round[1] + "] foi vencida!"  );
                            winner = round[1];
                            break whileLoop;
                        } else {
                            msgToAll(round[1] + "º round foi ganho com " + manilha.getValue() + manilha.getNipe());
                        }
                    } else if (i == 2) {
                        if (round[0].equals(round[2]) || round[1].equals(round[2])) {
                            winner = round[2];
                        }
                        msgToAll("[Rodada " + round[2] + "] foi vencida!"  );
                    }

                    manilha = new Card();
                }

                isTruco = false;

                if (winner.equals("Time 1")) {
                    playerOnePoints = playerOnePoints + roundValue;
                } else {
                    playerTwoPoints = playerTwoPoints + roundValue;
                }

                roundValue = 2;

                Deck.shuffleCards();

            }
            for (Player p : players) {
                p.sendMsg(gson.toJson(new Data(5, "\nO ganhador foi " + (playerOnePoints > playerTwoPoints ? "Time 1" : "Time 2"))));
                p.socket.close();
            }

            socketServer.close();
            System.out.println("[SERVIDOR] Conexão finalizada...");

        } catch (Exception e) {
            System.out.println("[SERVIDOR] Erro: \n" + e.toString());
        }
    }
}
