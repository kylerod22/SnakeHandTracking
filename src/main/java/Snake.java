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
        GamePanel.board[headYPos][headXPos] = 1;
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
        if (!grow) { //If the snake doesn't grow, pop the tail.
            int[] tailCoords = bodyList.getLast();
            GamePanel.board[tailCoords[1]][tailCoords[0]] = 0;
            bodyList.removeLast();
        }

        headXPos += dx; headYPos += dy; //Insert the head at the front of the Linked List
        GamePanel.board[headYPos][headXPos] = 1;
        bodyList.addFirst(new int[] {headXPos, headYPos});
        //System.out.println(bodyList.size());

        print();
    }

    public boolean ateFood(int foodX, int foodY) {
        return GamePanel.compareCoordinates(new int[] {headXPos, headYPos},new int[] {foodX, foodY});
    }

    public void print() {
        for (int[] bodyCoord : bodyList) {
            System.out.print(bodyCoord[0] + ", " + bodyCoord[1] + " | ");
        }
        System.out.print('\n');
    }

}
