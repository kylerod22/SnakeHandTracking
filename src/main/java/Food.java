import java.util.Random;

public class Food {
    int xPos, yPos;

    public Food() {
        Random rand = new Random();
        boolean occupiesSnake = true;
        while (occupiesSnake) {
            xPos = rand.nextInt(Game.WIDTH);
            yPos = rand.nextInt(Game.HEIGHT);
            occupiesSnake = false;
            for (int[] bodyCoord : Snake.bodyList) {
                if (GamePanel.compareCoordinates(new int[]{xPos, yPos}, bodyCoord)) {
                    occupiesSnake = true;
                    break;
                }
            }
        }
    }
}
