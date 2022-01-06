import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;


public class GamePanel extends JPanel implements Runnable {
    private final int pixelSize = 16;
    public static int height = 20, width = 20, camId = 0, points = 0;
    public static boolean camInverted = false;
    private int scale = 2, delayMillis = 200;

    KeyHandler keyHandler = new KeyHandler();

    final Color[] gameColors = {Color.GREEN, Color.RED, Color.CYAN}; //Snake Color, Food Color, SnakeCPU color

    public static Snake snake;
    public static SnakeCPU snakeOpponent;
    public static ArrayList<Snake> snakePlayers = new ArrayList<>();
    public static Food food;
    public static boolean runGame = true, hasOpponent = false;

    PyProcess p;

    public GamePanel() {
        initConfig();
        final int screenWidth = scale * pixelSize * width;
        final int screenHeight = scale * pixelSize * height;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGame() {
        initListenerThread();
        snake = new Snake(hasOpponent);
        snakePlayers.add(snake);
        if (hasOpponent) snakeOpponent = new SnakeCPU(); snakePlayers.add(snakeOpponent);
        food = new Food();
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        int initTime = (int) System.currentTimeMillis();
        repaint();
        while (runGame) {
            if ((int) System.currentTimeMillis() - initTime >= delayMillis) {
                initTime = (int) System.currentTimeMillis();
                for (Snake currSnake : snakePlayers) currSnake.update();
                repaint();
            }
        }
        p.exit();
    }

    public void update() {
        String strDir = p.tryRead();
        if (strDir != null && !strDir.equals("")) {
            snake.setDirection(strDir);
        }
    }

    public void initConfig() {
        Properties prop = new Properties();
        try {
            InputStream configInput = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (configInput != null) {
                prop.load(configInput);
                width = Integer.parseInt(prop.getProperty("Width"));
                height = Integer.parseInt(prop.getProperty("Height"));
                delayMillis = Integer.parseInt(prop.getProperty("DelayMillis"));
                hasOpponent = Boolean.parseBoolean(prop.getProperty("HasOpponent"));
                scale = Integer.parseInt(prop.getProperty("Scale"));
                camId = Integer.parseInt(prop.getProperty("CamId"));
                camInverted = Boolean.parseBoolean(prop.getProperty("CamInverted"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initListenerThread() {
        p = new PyProcess(System.getProperty("user.dir") + "/src/main/python/hand_direction_detection.py");
        Thread pyListenerThread = new Thread(() -> {
            while (runGame) {
                update();
            }
        });
        pyListenerThread.start();
    }

//    public void update() {
//        if (keyHandler.leftPressed) {
//            if (snake.currDirection != Snake.direction.RIGHT) {
//                snake.currDirection = Snake.direction.LEFT;
//                snake.dx = -1; snake.dy = 0;
//            }
//        }
//        if (keyHandler.rightPressed) {
//            if (snake.currDirection != Snake.direction.LEFT) {
//                snake.currDirection = Snake.direction.RIGHT;
//                snake.dx = 1; snake.dy = 0;
//            }
//        }
//        if (keyHandler.downPressed) {
//            if (snake.currDirection != Snake.direction.UP) {
//                snake.currDirection = Snake.direction.DOWN;
//                snake.dx = 0; snake.dy = 1;
//            }
//        }
//        if (keyHandler.upPressed) {
//            if (snake.currDirection != Snake.direction.DOWN) {
//                snake.currDirection = Snake.direction.UP;
//                snake.dx = 0; snake.dy = -1;
//            }
//        }
//    }

    public void paintComponent(Graphics g) {
        if (food == null || snake == null) return;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(gameColors[1]);
        g2.fillRect(scale * pixelSize * food.xPos, scale * pixelSize * food.yPos,
                scale * pixelSize, scale * pixelSize);

        g2.setColor(gameColors[0]);
        for (int[] bodyCoord : snake.bodyList) {
            g2.fillRect(scale * pixelSize * bodyCoord[0], scale * pixelSize * bodyCoord[1],
                    scale * pixelSize, scale * pixelSize);
        }

        if (hasOpponent) {
            g2.setColor(gameColors[2]);
            for (int[] bodyCoord : snakeOpponent.bodyList) {
                g2.fillRect(scale * pixelSize * bodyCoord[0], scale * pixelSize * bodyCoord[1],
                        scale * pixelSize, scale * pixelSize);
            }
        }

    }

    public static boolean compareCoordinates(int[] first, int[] second) {
        return (first[0] == second[0] && first[1] == second[1]);
    }
}
