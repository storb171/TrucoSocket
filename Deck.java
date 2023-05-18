import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private static List<Card> deck = new ArrayList<Card>();

    private static void createDeck() {
        String[] naipes = { "♦", "♥", "♣", "♠" };
        String[] valores = { "A", "2", "3", "4", "5", "6", "7", "J", "Q", "K" };

        for (String naipe : naipes) {
            for (String valor : valores) {
                deck.add(new Card(naipe, valor));
            }
        }
    }

    public Card getManilha(){
        Card manilha = deck.get(0);
        deck.remove(0);
        Card.setManilha(manilha);
        Card strongestCard = manilha;
        return strongestCard;
    }
    
    public static void shuffleCards() {
        if (deck.size() != 40) {
            createDeck();
        }
        Collections.shuffle(deck);
    }

    public static List<Card> addCards() {
        List<Card> playerHand = new ArrayList<Card>();
        for (int i = 0; i < 3; i++) {
            playerHand.add(deck.get(i));
            deck.remove(i);
        }
        return playerHand;
    }
}
