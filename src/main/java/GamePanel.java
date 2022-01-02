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


    public static int[][] board;

    private static int points = 1;
    Snake snake;

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
        snake = new Snake();
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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    }

    public static boolean compareCoordinates(int[] first, int[] second) {
        return (first[0] == second[0] && first[1] == second[1]);
    }


}
