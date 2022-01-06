import java.util.LinkedList;

public class SnakeCPU extends Snake {
    public SnakeCPU() {
        super(true);
        headXPos = GamePanel.width / 4 * 3;
        headYPos = GamePanel.height / 4 * 3;
        bodyList = new LinkedList<>();
        bodyList.addFirst(new int[] {headXPos, headYPos});
    }

    private double distToFood(int dx, int dy) {
        return Math.sqrt(Math.pow(headXPos + dx - GamePanel.food.xPos, 2) + Math.pow(headYPos + dy - GamePanel.food.yPos, 2));
    }

    private int[] getDxDyFromDirection(direction inputDir) {
        switch (inputDir) {
            default -> {
                return new int[] {0, 0};
            }
            case LEFT -> {
                return new int[] {-1, 0};
            }
            case RIGHT -> {
                return new int[] {1, 0};
            }
            case DOWN -> {
                return new int[] {0, 1};
            }
            case UP -> {
                return new int[] {0, -1};
            }
        }
    }

    public void setBestDirection() {
        double shortestDist = distToFood(dx, dy);
        direction bestDirection = currDirection;
        for (direction testDir : direction.values()) {
            if (testDir == direction.STATIONARY) continue;
            int[] testDxDy = getDxDyFromDirection(testDir);
            double testDist = distToFood(testDxDy[0], testDxDy[1]);
            //System.out.println(testDir + " " + canMove(testDxDy[0], testDxDy[1]));
            if (testDist <= shortestDist && canMove(testDxDy[0], testDxDy[1])) {
                bestDirection = testDir;
                shortestDist = testDist;
            }
        }

        int[] bestDxDy = getDxDyFromDirection(bestDirection);
        if (!canMove(bestDxDy[0], bestDxDy[1])) {
            for (direction testDir : direction.values()) {
                int[] testDxDy = getDxDyFromDirection(testDir);
                if (canMove(testDxDy[0], testDxDy[1])) bestDirection = testDir;
            }
        }
        setDirection(bestDirection.name());
    }

    public void update() {
        if (GamePanel.snake.currDirection != direction.STATIONARY) {
            setBestDirection();
            if (!canMove(dx, dy)) {GamePanel.runGame = false; return;}
            move(ateFruit);
            ateFruit = ateFood(GamePanel.food);
            if (ateFruit) {
                GamePanel.food = new Food();
                GamePanel.points++;
            }
        }
    }
}
