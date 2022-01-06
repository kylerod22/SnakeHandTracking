import java.util.Random;

public class Food {
    int xPos, yPos;

    public Food() {
        Random rand = new Random();
        boolean occupiesSnake = true;
        while (occupiesSnake) {
            xPos = rand.nextInt(GamePanel.width);
            yPos = rand.nextInt(GamePanel.height);
            occupiesSnake = false;
            for (Snake currSnake : GamePanel.snakePlayers) {
                for (int[] bodyCoord : currSnake.bodyList) {
                    if (GamePanel.compareCoordinates(new int[]{xPos, yPos}, bodyCoord)) {
                        occupiesSnake = true;
                        break;
                    }
                }
            }

        }
    }
}
