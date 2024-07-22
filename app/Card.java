package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Card(Suit suit, String face, int rank) {
    public enum Suit {
        CLUB, DIAMOND, HEART, SPADE, NONE;

        public char getImage() {
            return (new char[] { 9827, 9830, 9829, 9824, 9831 })[this.ordinal()];
        }
    }

    @Override
    public String toString() {

        int index = face.equals("10") ? 2 : 1;
        String faceString = face.substring(0, index);
        return "%s%c (%s)".formatted(faceString, suit.getImage(),
                suit.toString().toLowerCase());
    }

    public static Card getNumericCard(Suit suit, int cardNumber) {
        if (cardNumber < 2 || cardNumber >= 11) {
            System.out.println("Invalid card number selected");
            return null;
        }

        return new Card(suit, String.valueOf(cardNumber), cardNumber - 2);
    }

    public static Card getFaceCard(Suit suit, char abbrev) {
        int charIndex = "JQKA".indexOf(abbrev);
        if (charIndex < 0) {
            System.out.println("Invalid card face");
            return null;
        }
        return new Card(suit, "" + abbrev, charIndex + 9);
    }

    public static Card getFoolCard() {
        return new Card(Suit.NONE, "F", 13);
    }

    public static List<Card> getStandardDeck() {
        List<Card> standardDeck = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            if (suit == Suit.NONE)
                continue;
            for (int i = 2; i <= 10; i++)
                standardDeck.add(getNumericCard(suit, i));

            for (char c : new char[] { 'J', 'Q', 'K', 'A', })
                standardDeck.add(getFaceCard(suit, c));
        }
        // standardDeck.addAll(Arrays.asList(getFoolCard(), getFoolCard()));
        for (int i = 0; i < 2; i++) {
            standardDeck.add(getFoolCard());
        }

        return standardDeck;
    }

    public static void printDeck(List<Card> deck, String description, int rows) {
        System.out.println("-------------------------------------------");
        if (description != null)
            System.out.println(description);

        int cardsInRow = deck.size() / rows;
        for (int i = 0; i < rows; i++) {
            int startIndex = i * cardsInRow;
            int endIndex = startIndex + cardsInRow;
            deck.subList(startIndex, endIndex).forEach(c -> System.out.print(c + " "));
            System.out.println();

        }
    }

    public static void printDeck(List<Card> deck) {
        printDeck(deck, "Current Deck", 4);
    }

}
