import javax.swing.*;

public class Game {

    public final String NAME = "Snake";
    public static final int WIDTH = 20, HEIGHT = 20;


    public Game() {

        JFrame f = new JFrame(NAME);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);

        GamePanel panel = new GamePanel();
        f.add(panel);
        f.pack();
        f.setLocationRelativeTo(null);

        f.setVisible(true);

        panel.startGame();
    }

    public static void main(String[] args) {
        new Game();
    }
}


