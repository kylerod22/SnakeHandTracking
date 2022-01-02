import java.util.LinkedList;

public class Snake {
    int headXPos, headYPos;
    int dx = 0, dy = 0;
    direction currDirection = direction.STATIONARY;
    public static LinkedList<int[]> bodyList;

    public enum direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        STATIONARY
    }

    public Snake() {
        headXPos = Game.WIDTH / 2;
        headYPos = Game.WIDTH / 2;
        bodyList = new LinkedList<>();
        bodyList.addFirst(new int[] {headXPos, headYPos});
    }

    public boolean canMove() {
        if (currDirection == direction.STATIONARY) return true;
        if (headXPos + dx >= 0 && headXPos + dx < Game.WIDTH && headYPos + dy >= 0 && headYPos + dy < Game.HEIGHT) {
            for (int[] bodyCoord : bodyList) {
                if (GamePanel.compareCoordinates(new int[]{headXPos + dx, headYPos + dy}, bodyCoord)) {

                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void move(boolean grow) {
        if (!grow) bodyList.removeLast(); //If the snake doesn't grow, pop the tail
        headXPos += dx; headYPos += dy; //Push the head to the front of the Linked List
        bodyList.addFirst(new int[] {headXPos, headYPos});
    }

    public boolean ateFood(int foodX, int foodY) {
        return GamePanel.compareCoordinates(new int[] {headXPos, headYPos},new int[] {foodX, foodY});
    }
}
