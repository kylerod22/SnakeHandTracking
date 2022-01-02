import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class GamePanel extends JPanel implements Runnable {
    private final int pixelSize = 16;
    private final int scale = 2;
    private final int screenWidth = scale * pixelSize * Game.WIDTH;
    private final int screenHeight = scale * pixelSize * Game.HEIGHT;

    KeyHandler keyHandler = new KeyHandler();

    Thread gameThread;
    int delayMillis = 100;


    public static int[][] board;
    final Color[] gameColors = {Color.GREEN, Color.RED}; //Snake Color, Food Color

    private static int points = 1;
    Snake snake;
    Food food;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        board = new int[Game.HEIGHT][Game.WIDTH];
        snake = new Snake();
        boolean runGame = true;
        boolean ateFruit;
        int initTime = (int) System.currentTimeMillis();
        food = new Food();
        repaint();

        while (runGame) {
            if ((int) System.currentTimeMillis() - initTime >= delayMillis) {
                update();
                if (!snake.canMove()) {
                   runGame = false;
                }

                initTime = (int) System.currentTimeMillis();
                ateFruit = snake.ateFood(food.xPos, food.yPos);
                if (ateFruit) {
                    food = new Food();
                }
                if (snake.canMove()) {
                    snake.move(ateFruit);
                }

                repaint();
            }
        }


    }

    public void update() {
        if (keyHandler.leftPressed) {
            snake.dx = -1; snake.dy = 0;
        }
        if (keyHandler.rightPressed) {
            snake.dx = 1; snake.dy = 0;
        }
        if (keyHandler.downPressed) {
            snake.dx = 0; snake.dy = 1;
        }
        if (keyHandler.upPressed) {
            snake.dx = 0; snake.dy = -1;
        }
    }

    public void paintComponent(Graphics g) {
        if (board == null) return;

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        for (int i = 0; i < Game.HEIGHT; i++) {
            for (int j = 0; j < Game.WIDTH; j++) {
                if (board[i][j] > 0) {
                    g2.setColor(gameColors[board[i][j] - 1]);
                    g2.fillRect(scale * pixelSize * j, scale * pixelSize * i,
                            scale * pixelSize, scale * pixelSize);
                }
            }
        }
    }

    public static boolean compareCoordinates(int[] first, int[] second) {
        return (first[0] == second[0] && first[1] == second[1]);
    }

    public void print() {
        for (int i = 0; i < Game.HEIGHT; i++) {
            for (int j = 0; j < Game.WIDTH; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print('\n');
        }
        System.out.println("-----------\n");
    }


}
