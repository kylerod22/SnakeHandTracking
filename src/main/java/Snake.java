import java.util.LinkedList;

public class Snake {
    int headXPos, headYPos;
    int dx = 1, dy = 0;
    public static LinkedList<int[]> bodyList;

    public Snake() {
        headXPos = Game.WIDTH / 2;
        headYPos = Game.WIDTH / 2;
        bodyList = new LinkedList<>();
        bodyList.addFirst(new int[] {headXPos, headYPos});
    }

    public boolean canMove() {
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
