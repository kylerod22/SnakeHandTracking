import java.util.ArrayList;
import java.util.LinkedList;

public class SnakeCPU extends Snake {
    private ArrayList<int[]> visited;
    private boolean doMove = false;
    public SnakeCPU() {
        super(true);
        visited = new ArrayList<>();
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

    private void setBestDirection() {
        direction bestDirection = direction.LEFT;
        int[] bestDxDy = getDxDyFromDirection(bestDirection);
        double bestDistance = distToFood(bestDxDy[0], bestDxDy[1]);//Shortest distance to food takes priority
        for (direction testDir : direction.values()) {
            if (testDir == direction.STATIONARY) continue;
            int[] testDxDy = getDxDyFromDirection(testDir);
            double testDist = distToFood(testDxDy[0], testDxDy[1]);
            if (testDist < bestDistance && canMove(headXPos, headYPos, testDxDy[0], testDxDy[1])) {
                bestDirection = testDir;
                bestDistance = testDist;
            }
        }

        visited.removeAll(visited);
        bestDxDy = getDxDyFromDirection(bestDirection);
        int bestOpenSpots = findOpenSpots(headXPos + bestDxDy[0], headYPos + bestDxDy[1]); //Next, find the more open area
        for (direction testDir : direction.values()) {
            if (testDir == direction.STATIONARY) continue;
            visited.removeAll(visited);
            int[] testDxDy = getDxDyFromDirection(testDir);
            if (canMove(headXPos, headYPos, testDxDy[0], testDxDy[1])) {
                int testOpenSpots = findOpenSpots(headXPos + testDxDy[0], headYPos + testDxDy[1]);
                if (testOpenSpots > bestOpenSpots) {
                    bestDirection = testDir;
                    bestOpenSpots = testOpenSpots;
                }
            }
        }
        
        bestDxDy = getDxDyFromDirection(bestDirection); //Finally, check if it can move in that direction
        if (!canMove(headXPos, headYPos, bestDxDy[0], bestDxDy[1])) {
            for (direction testDir : direction.values()) {
                if (testDir == direction.STATIONARY) continue;
                int[] testDxDy = getDxDyFromDirection(testDir);
                if (canMove(headXPos, headYPos, testDxDy[0], testDxDy[1])) bestDirection = testDir;
            }
        }
        setDirection(bestDirection.name());
    }

    private int findOpenSpots(int initX, int initY) {
        int count = 1;
        visited.add(new int[] {initX, initY});
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 ^ dy == 0) {
                    //System.out.println(initX + " " + initY + " " + dx + " " + dy + " " + canMove(initX, initY, dx, dy));
                    if (canMove(initX, initY, dx, dy)) {
                        int nextX = initX + dx, nextY = initY + dy;
                        //System.out.println(nextX + " " + nextY);
                        if (!visited(nextX, nextY)) count += findOpenSpots(nextX, nextY);
                    }
                }
            }
        }
        return count;
    }

    private boolean visited(int testX, int testY) {
        for (int[] coord : visited) {
            if (GamePanel.compareCoordinates(new int[] {testX, testY}, coord)) return true;
        }
        return false;
    }

    public void update() {
        doMove = !doMove;
        if (GamePanel.snake.currDirection != direction.STATIONARY && doMove) {
            setBestDirection();
            if (!canMove(headXPos, headYPos, dx, dy)) {GamePanel.runGame = false; return;}
            move(ateFruit);
            ateFruit = ateFood(GamePanel.food);
            if (ateFruit) {
                GamePanel.food = new Food();
                GamePanel.points++;
            }
        }
    }
}
