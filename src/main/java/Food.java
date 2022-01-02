import java.util.Random;

public class Food {
    int xPos, yPos;

    public Food() {
        Random rand = new Random();
        boolean flag = true;
        while (flag) {
            xPos = rand.nextInt(Game.WIDTH);
            yPos = rand.nextInt(Game.HEIGHT);
            flag = false;
            for (int[] bodyCoord : Snake.bodyList) {
                if (GamePanel.compareCoordinates(new int[]{xPos, yPos}, bodyCoord)) {
                    flag = true;
                    break;
                }
            }
        }
    }
}
