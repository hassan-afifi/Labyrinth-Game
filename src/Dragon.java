import java.util.Random;

public class Dragon {
    protected int x;
    protected int y;
    private int dx;
    private int dy;
    private Random random;
    
    /*
    @param int, int
    @return
    */
    public Dragon(int x, int y) {
        this.x = x;
        this.y = y;
        this.random = new Random();
        chooseRandomDirection();
    }
    
    /*
    @param boolean[][], int, int
    @return
    */
    public void move(boolean[][] walls, int gridWidth, int gridHeight) {
        int newX = x + dx;
        int newY = y + dy;
        
        if (newX < 1 || newX >= gridWidth - 1 || newY < 1 || newY >= gridHeight - 1 || walls[newX][newY]) {
            chooseRandomDirection();
        }
        else {
            x = newX;
            y = newY;
        }
    }
    
    /*
    @param
    @return
    */
    private void chooseRandomDirection() {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[] dir = directions[random.nextInt(directions.length)];
        dx = dir[0];
        dy = dir[1];
    }
}
