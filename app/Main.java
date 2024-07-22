package app;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminal.clear();
        Scanner scanner = new Scanner(System.in);
        Pesten game = new Pesten(scanner,
                Arrays.asList(new Player(), new Player(), new Player(), new Player()));

        game.startGame(9);
    }
}
