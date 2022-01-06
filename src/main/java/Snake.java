import java.util.LinkedList;

public class Snake {
    int headXPos, headYPos;
    int dx = 0, dy = 0;
    direction currDirection = direction.STATIONARY;
    public LinkedList<int[]> bodyList;
    boolean ateFruit = false;

    public enum direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        STATIONARY
    }

    public Snake(boolean hasOpponent) {
        if (hasOpponent) {
            headXPos = GamePanel.width / 4;
            headYPos = GamePanel.height / 4;
        } else {
            headXPos = GamePanel.width / 2;
            headYPos = GamePanel.height / 2;
        }
        bodyList = new LinkedList<>();
        bodyList.addFirst(new int[] {headXPos, headYPos});
    }

    public boolean canMove(int inDx, int inDy) {
        if (currDirection == direction.STATIONARY) return true;
        if (headXPos + inDx >= 0 && headXPos + inDx < GamePanel.width && headYPos + inDy >= 0 && headYPos + inDy < GamePanel.height) {
            for (Snake currSnake : GamePanel.snakePlayers) {
                for (int[] bodyCoord : currSnake.bodyList) {
                    if (GamePanel.compareCoordinates(new int[]{headXPos + inDx, headYPos + inDy}, bodyCoord)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void setDirection(String inputDirection) {
        try {
            direction proposedDirection = direction.valueOf(inputDirection);
            switch (proposedDirection) {
                case LEFT:
                    if (currDirection != direction.RIGHT) {
                        currDirection = direction.LEFT;
                        dx = -1; dy = 0;
                    }
                    break;
                case RIGHT:
                    if (currDirection != direction.LEFT) {
                        currDirection = direction.RIGHT;
                        dx = 1; dy = 0;
                    }
                    break;
                case DOWN:
                    if (currDirection != direction.UP) {
                        currDirection = direction.DOWN;
                        dx = 0; dy = 1;
                    }
                    break;
                case UP:
                    if (currDirection != direction.DOWN) {
                        currDirection = direction.UP;
                        dx = 0; dy = -1;
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move(boolean grow) {
        if (!grow) bodyList.removeLast(); //If the snake doesn't grow, pop the tail
        headXPos += dx; headYPos += dy; //Push the head to the front of the Linked List
        bodyList.addFirst(new int[] {headXPos, headYPos});
    }

    public boolean ateFood(Food food) {
        return GamePanel.compareCoordinates(new int[] {headXPos, headYPos},new int[] {food.xPos, food.yPos});
    }

    public void update() {
        if (!canMove(dx, dy)) GamePanel.runGame = false;
        ateFruit = ateFood(GamePanel.food);
        if (ateFruit) {
            GamePanel.food = new Food();
            GamePanel.points++;
        }
        if (canMove(dx, dy)) move(ateFruit);

    }
}
