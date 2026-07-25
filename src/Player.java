public class Player {
    protected int x;
    protected int y;
    
    /*
    @param int, int
    @return
    */
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*
    @param int, int, boolean[][], int, int
    @return
    */
    public void move(int dx, int dy, boolean[][] walls, int gridWidth, int gridHeight) {
        int newX = x + dx;
        int newY = y + dy;
        
        if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && !walls[newX][newY]) {
            x = newX;
            y = newY;
        }
    }
}
