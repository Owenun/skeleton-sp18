package byog.lab6;

import byog.lab5.HexWorld;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdRandom;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MemoryGame {
    private final int width;
    private final int height;
    private int round;
    private final Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        long seed = in.nextLong();

        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n) {
            sb.append(CHARACTERS[rand.nextInt(26)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        int middleX = width / 2;
        int middleY = height / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        if (!gameOver ) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round " + round);
            StdDraw.text(middleX, height - 1, playerTurn ? "Type" : "Watch");
            String encourage = ENCOURAGEMENT[round % ENCOURAGEMENT.length];
            StdDraw.textRight(width, height - 1 , encourage);
            StdDraw.line(0 ,height - 2, width, height - 2);
        }

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(middleX, middleY, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        int i = 0;
        while ( i < letters.length()) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame(" ");
            StdDraw.pause(500);
            i++;
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StringBuilder sb = new StringBuilder();
        drawFrame(" ");

        while ( sb.length() < n) {
            if (! StdDraw.hasNextKeyTyped()) continue;

            sb.append(StdDraw.nextKeyTyped());
            drawFrame(sb.toString());
        }

        StdDraw.pause(1000);

        return sb.toString();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        round = 1;
        gameOver = false;
        playerTurn = false;

        //TODO: Establish Game loop
        while (!gameOver) {

            playerTurn = false;

            drawFrame("Round:" + round + "Good Luck!" );
            StdDraw.pause(1000);

            String letters = generateRandomString(round);
            flashSequence(letters);

            playerTurn = true;
            String sb = solicitNCharsInput(round);

            if (sb.equals(letters)) {
                drawFrame("Great! You type right letters!");
                round++;
            } else {
                drawFrame("Sorry, you type wrong letters!");
                gameOver = true;
            }
        }

        drawFrame("Game Over! " + "Final Round Level: " + round + "!" );
    }

}
