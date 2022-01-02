
import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    private final int pixelSize = 16;
    private final int scale = 2;

    KeyHandler keyHandler = new KeyHandler();
    int delayMillis = 100;

    final Color[] gameColors = {Color.GREEN, Color.RED}; //Snake Color, Food Color

    private int points = 1;
    static Snake snake;
    Food food;

    public GamePanel() {
        final int screenWidth = scale * pixelSize * Game.WIDTH;
        final int screenHeight = scale * pixelSize * Game.HEIGHT;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGame() {
        snake = new Snake();
        food = new Food();
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        boolean runGame = true;
        boolean ateFruit;
        int initTime = (int) System.currentTimeMillis();
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
                    points++;
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
            if (snake.currDirection != Snake.direction.RIGHT) {
                snake.currDirection = Snake.direction.LEFT;
                snake.dx = -1; snake.dy = 0;
            }
        }
        if (keyHandler.rightPressed) {
            if (snake.currDirection != Snake.direction.LEFT) {
                snake.currDirection = Snake.direction.RIGHT;
                snake.dx = 1; snake.dy = 0;
            }
        }
        if (keyHandler.downPressed) {
            if (snake.currDirection != Snake.direction.UP) {
                snake.currDirection = Snake.direction.DOWN;
                snake.dx = 0; snake.dy = 1;
            }
        }
        if (keyHandler.upPressed) {
            if (snake.currDirection != Snake.direction.DOWN) {
                snake.currDirection = Snake.direction.UP;
                snake.dx = 0; snake.dy = -1;
            }
        }
    }

    public void paintComponent(Graphics g) {
        if (food == null && snake == null) return;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(gameColors[1]);
        g2.fillRect(scale * pixelSize * food.xPos, scale * pixelSize * food.yPos,
                scale * pixelSize, scale * pixelSize);

        g2.setColor(gameColors[0]);
        for (int[] bodyCoord : Snake.bodyList) {
            g2.fillRect(scale * pixelSize * bodyCoord[0], scale * pixelSize * bodyCoord[1],
                    scale * pixelSize, scale * pixelSize);
        }
    }

    public static boolean compareCoordinates(int[] first, int[] second) {
        return (first[0] == second[0] && first[1] == second[1]);
    }
}
