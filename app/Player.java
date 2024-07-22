package app;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Card> cards;
    private String name;
    private static int id = 0;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player() {
        id++;
        String name = "Player #000" + id;
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public void showHand() {
        System.out.println("\nHand from " + name + ":" + cards.size());
        System.out.println("--------------------------------------");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(cards.get(i) + " \tIndex: " + i);
        }
        System.out.println();

    }

    public void addCardToHand(Card card) {
        cards.add(card);
    }

    public void addCardToHand(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public void removeCardFromHand(Card card) {
        cards.remove(card);
    }

    @Override
    public String toString() {
        return "Player: " + name;
    }

}
