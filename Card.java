import java.util.HashMap;
import java.util.Map;

public class Card {

    String nipe;
    String valor;

    private static Card manilha;
    
    public static void setManilha(Card cartaManilha) {
        manilha = cartaManilha;
    }
    
    public static Card getManilha() {
        return manilha;
    }

    public Card() {
        nipe = "";
        valor = "";
    }

    public Card(String nipe, String valor) {
        this.nipe = nipe;
        this.valor = valor;
    }

    public String getNipe() {
        return nipe;
    }

    public String getValue() {
        return valor;
    }

    public void setValue(String nipe) {
        this.nipe = nipe;
    }

    public void setNipe(String valor) {
        this.valor = valor;
    }

    public static Card valuesOfCards(Card a, Card b) {
        Map<String, Integer> cardValue = new HashMap<>();
        cardValue.put("3", 13);
        cardValue.put("2", 12);
        cardValue.put("A", 11);
        cardValue.put("K", 10);
        cardValue.put("Q", 9);
        cardValue.put("J", 8);
        cardValue.put("7", 7);
        cardValue.put("6", 6);
        cardValue.put("5", 5);
        cardValue.put("4", 4);

        int valorA = cardValue.getOrDefault(a.getValue(), 0);
        int valorB = cardValue.getOrDefault(b.getValue(), 0);

        if (valorA > valorB) {
            return a;
        } else if (valorB > valorA) {
            return b;
        } else {
            return new Card("", "");
        }
    }
}