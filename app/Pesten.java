package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Pesten {
    private List<Card> deck;
    private List<Card> cardsOnTable;
    private List<Player> players;
    private Card.Suit currentSuit;
    private Scanner scanner;
    private boolean isGame;
    private boolean debug = true;
    private boolean skipNext = false;
    private boolean reversed = false;
    private int currentPlayerIndex = 0;

    private void debug(String input) {
        if (!debug)
            return;
        System.out.println("[!] DEBUG:: " + input);
    }

    public Pesten(Scanner scanner, List<Player> players) {
        System.out.println("===============================================\n");
        System.out.println("\tWelcome to a game of pesten!!\n");
        System.out.println("===============================================\n");

        this.scanner = scanner;
        this.players = players;
        cardsOnTable = new ArrayList<>();
        deck = new ArrayList<>(Card.getStandardDeck());
    }

    private void dealInitialCards(int amountOfCards) {
        System.out.println("[+] Dealing " + amountOfCards + " cards to players\n");
        for (int i = 0; i < amountOfCards; i++) {
            for (Player player : players) {
                player.addCardToHand(deck.remove(0));
            }
        }
    }

    private void mergeCardsOnTableWithDeck() {

        deck.addAll(cardsOnTable);

        if (!cardsOnTable.isEmpty())
            cardsOnTable.clear();
        drawInitialcard();

    }

    private Card getCurrentCardOnTop() {
        int amountOfCardsOnTable = cardsOnTable.size();
        debug("Amount of cards on table:" + amountOfCardsOnTable);
        return cardsOnTable.get(cardsOnTable.size() - 1);
    }

    private void showCurrentCard() {
        Card currentCardOnTop = getCurrentCardOnTop();
        System.out.println("\n Current card on top:");
        debug("Show current card:");
        System.out.println("----------------------------------------");
        System.out.println("Current card on top: " + currentCardOnTop);
        System.out.println("Current suit: " + currentSuit + "\n");
    }

    private void dealCard(Player player) {
        if (deck.isEmpty()) {
            mergeCardsOnTableWithDeck();
            if (deck.size() < 3)
                deck.addAll(Card.getStandardDeck());
        }
        player.addCardToHand(deck.remove(0));
    }

    private void inferActionFromPlayer(Player player) {

        System.out.println(
                "\nTo use a card, type: \"play\", to draw a card, type: \"draw\"");

        String inputSelection = scanner.nextLine();

        if (inputSelection.equalsIgnoreCase("play")) {
            if (playCard(getCardFromIndex(player), player)) {
                return;
            }
        }
        dealCard(player);

    }

    private Card getCardFromIndex(Player player) {
        int indexOfCardToPlay;
        Card cardToPlay;
        while (true) {
            System.out.println("Type a valid index:");

            String input = scanner.nextLine();

            try {
                indexOfCardToPlay = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Not a valid integer!");
                continue;
            }

            try {

                cardToPlay = player.getCards().get(indexOfCardToPlay);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Not a valid index..");
                continue;
            }

            System.out.println("\n============================");
            System.out.println("Playing card: " + cardToPlay);
            debug("From index: " + indexOfCardToPlay);
            return cardToPlay;
        }
    }

    private boolean playCard(Card card, Player player) {

        System.out.println();
        debug("Current suit" + currentSuit);
        debug("card suit: " + card.suit());
        debug("is same suit: " + (card.suit() == currentSuit));
        if (!isValidCardToPlay(card)) {
            System.out.println("!!!! INVALID SUIT  OR NUMBER !!!");
            return false;
        }
        debug("playing card....");
        debug(player.toString());
        debug("Card: " + card);
        debug("Suit of card: " + card.suit());
        // showCurrentCard();

        removeCardFromPlayerAndAddToCardOnTable(card, player);
        applyCardAction(card, player);
        return true;

    }

    private void removeCardFromPlayerAndAddToCardOnTable(Card card, Player player) {
        debug("Amount of cards in player hands:" + player.getCards().size());
        player.removeCardFromHand(card);
        debug("Amount of cards in player hands:" + player.getCards().size());
        debug("amount of cards on table: " + cardsOnTable.size());
        cardsOnTable.add(card);
        debug("amount of cards on table: " + cardsOnTable.size());

    }

    private void applyCardAction(Card card, Player currentPlayer) {

        // TODO: use nextPlayerIndex for both variables.

        // int indexForNextPlayer = players.indexOf(currentPlayer) + 1;
        // int playerIndexRange = players.size() - 1;
        // int nextPlayerIndex = indexForNextPlayer > playerIndexRange ? 0
        // : indexForNextPlayer;
        // Player nextPlayer = players.get(nextPlayerIndex);

        int nextPlayerIndex = getNextIndex(currentPlayerIndex, players.size());
        Player nextPlayer = players.get(nextPlayerIndex);
        currentSuit = card.suit();

        switch (card.face()) {
            case "2":
                cardActionForFaceTwo(nextPlayer);
                break;
            case "7":
                cardActionForFaceSeven(currentPlayer);
                break;
            case "8":
                cardActionForFaceEight();
                break;
            case "A":
                cardActionForFaceAce();
                break;
            case "J":
                debug("Applying logic for J");
                cardActionForFaceJack();
                break;
            default:
                break;

        }
    }

    // Actions
    private void cardActionForFaceTwo(Player nextPlayer) {

        debug("Deck size before removing two cards: " + deck.size());

        List<Card> twoCardsForNextPlayer = new ArrayList<>(deck.subList(0, 2));
        deck.subList(0, 2).clear();
        debug("Deck size after removing two cards: " + deck.size());
        debug("total cards for next player: " + nextPlayer.getCards().size());
        nextPlayer.addCardToHand(twoCardsForNextPlayer);
        debug("total cards for next player after getting two: "
                + nextPlayer.getCards().size());
        return;
    }

    private void cardActionForFaceSeven(Player currentPlayer) {
        List<Card> allOfSuit = new ArrayList<>();

        debug("Amount of cards currently in hand: " + currentPlayer.getCards().size());
        for (Card c : currentPlayer.getCards()) {
            if (c.suit() == currentSuit) {
                allOfSuit.add(c);
                // currentPlayer.getCards().remove(c);
            }
        }

        currentPlayer.getCards().removeAll(allOfSuit);
        debug("Amount now in hand: %d, amount in all of suits:%d"
                .formatted(currentPlayer.getCards().size(), allOfSuit.size()));

        debug("Amount of cards on table: " + cardsOnTable.size());
        Collections.reverse(allOfSuit);
        cardsOnTable.addAll(allOfSuit);
        debug("Amount of cards on table after adding all of suit: "
                + cardsOnTable.size());
    }

    private void cardActionForFaceEight() {
        skipNext = true;
    }

    private void cardActionForFaceAce() {
        if (players.size() == 2) {
            skipNext = true;
            return;
        }
        reversed = !reversed;
    }

    private void cardActionForFaceJack() {

        int choice;
        Card.Suit newSuit;
        System.out.println("Change the suit of the cards.");
        System.out.println("(0) CLUB (1) DIAMOND (2) HEART (3) SPADE");

        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > 3)
                    continue;
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid integer..");
            }
        }

        switch (choice) {
            case 0:
                newSuit = Card.Suit.CLUB;
                break;
            case 1:
                newSuit = Card.Suit.DIAMOND;
                break;
            case 2:
                newSuit = Card.Suit.HEART;
                break;
            case 3:
                newSuit = Card.Suit.SPADE;
                break;
            default:
                newSuit = currentSuit;
                break;
        }

        debug("Current suit is: " + currentSuit);
        debug("Chosen suit is:" + newSuit + " (Int = " + choice + ")");
        currentSuit = newSuit;
        debug("Suit is now: " + currentSuit);

    }

    private boolean isValidCardToPlay(Card card) {
        debug("is valid card to play, face: " + card.face());
        Card currentCardOnTOp = getCurrentCardOnTop();
        debug("Current card on top face: " + currentCardOnTOp.face());
        debug("Equals?" + (card.face().equalsIgnoreCase(currentCardOnTOp.face())));
        if (card.face().equalsIgnoreCase("J"))
            return true;

        if (card.face().equals(currentCardOnTOp.face()))
            return true;

        // if (card.suit() != currentSuit && card.face() != currentCardOnTOp.face())
        if (card.suit() != currentSuit
                && !card.face().equalsIgnoreCase(currentCardOnTOp.face()))
            return false;

        return true;
    }

    private boolean isCurrentPlayerWinner(Player player) {
        return player.getCards().isEmpty();
    }

    private void drawInitialcard() {
        debug("Amount of cards on table: " + cardsOnTable.size());
        Card initalCard = deck.remove(0);
        cardsOnTable.add(initalCard);
        currentSuit = initalCard.suit();
        debug("Amount of cards on table: " + cardsOnTable.size());
    }

    public void startGame(int amountOfCards) {
        isGame = true;
        int amountOfPlayers = players.size();
        Collections.shuffle(deck);
        dealInitialCards(amountOfCards);
        drawInitialcard();

        while (isGame) {
            if (skipNext) {
                skipNext = false;
                currentPlayerIndex = getNextIndex(currentPlayerIndex, amountOfPlayers);
                continue;
            }
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("Current player: " + currentPlayer.getName());
            debug("All players: " + players);
            showCurrentCard();
            currentPlayer.showHand();

            inferActionFromPlayer(currentPlayer);
            if (isCurrentPlayerWinner(currentPlayer)) {
                System.out.println(
                        "\n\nWE HAVE A WINNER:\n\nPlayer: " + currentPlayer.getName());
                isGame = false;
            }
            System.out.println("Press a key to continue to next round...");
            scanner.nextLine();
            Terminal.clear();

            currentPlayerIndex = getNextIndex(currentPlayerIndex, amountOfPlayers);

        }
    }

    private int getNextIndex(int currentIndex, int amountOfPlayers) {
        if (reversed) {
            currentIndex--;
            if (currentIndex < 0)
                currentIndex = amountOfPlayers - 1;
        } else {
            currentIndex++;
            if (currentIndex >= amountOfPlayers)
                currentIndex = 0;
        }
        return currentIndex;
    }
    // public void startGame(int amountOfCards) {
    // isGame = true;
    // int amountOfPlayers = players.size();
    // Collections.shuffle(deck);
    // dealInitialCards(amountOfCards);
    // drawInitialcard();

    // while (isGame) {
    // for (int i = 0; i < amountOfPlayers; i++) {
    // if (skipNext) {
    // skipNext = false;
    // continue;
    // }
    // Player currentPlayer = players.get(i);
    // System.out.println("Current player: " + currentPlayer.getName());
    // debug("All players: " + players);
    // showCurrentCard();
    // currentPlayer.showHand();

    // inferActionFromPlayer(currentPlayer);
    // if (isCurrentPlayerWinner(currentPlayer)) {
    // System.out.println("\n\nWE HAVE A WINNER:\n\nPlayer: "
    // + currentPlayer.getName());
    // isGame = false;
    // }
    // System.out.println("Press a key to continue to next round...");
    // scanner.nextLine();
    // Terminal.clear();

    // }
    // }
    // }

}
