import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 30;
    private static final int GRID_WIDTH = 25;
    private static final int GRID_HEIGHT = 15;
    private static final int VISIBILITY_RANGE = 3;
    private LabyrinthGame parent;
    private Timer gameTimer;
    private Timer clockTimer;
    private Player player;
    private Dragon dragon;
    private boolean[][] walls;
    private boolean gameRunning;
    private LevelManager levelManager;
    private int currentTime;
    private int levelsCompleted;
    
    /*
    @param LabyrinthGame
    @return
    */
    public GamePanel(LabyrinthGame parent) {
        this.parent = parent;
        this.levelManager = new LevelManager();
        setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initializeGame();
        startGame();
    }
    
    /*
    @param
    @return
    */
    private void initializeGame() {
        loadRandomLevel();
        player = new Player(1, GRID_HEIGHT - 2);
        dragon = new Dragon(GRID_WIDTH - 2, 1);
        repositionDragon();
        gameRunning = true;
        currentTime = 0;
        levelsCompleted = 0;
        parent.updateTime(currentTime);
        parent.updateLevelsCompleted(levelsCompleted);
    }
    
    /*
    @param
    @return
    */
    private void loadRandomLevel() {
        try {
            walls = levelManager.loadRandomLevel();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading level: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            createFallbackLevel();
        }
    }
    
    /*
    @param
    @return
    */
    private void createFallbackLevel() {
        walls = new boolean[GRID_WIDTH][GRID_HEIGHT];
        
        for (int x = 0; x < GRID_WIDTH; x++) {
            walls[x][0] = true;
            walls[x][GRID_HEIGHT - 1] = true;
        }
        
        for (int y = 0; y < GRID_HEIGHT; y++) {
            walls[0][y] = true;
            walls[GRID_WIDTH - 1][y] = true;
        }
        
        for (int x = 1; x < GRID_WIDTH - 1; x++) {
            walls[x][GRID_HEIGHT / 2] = true;
        }
        
        walls[GRID_WIDTH / 2][GRID_HEIGHT / 2] = false;
    }
    
    /*
    @param
    @return
    */
    private void repositionDragon() {
        java.util.Random rand = new java.util.Random();
        int attempts = 0;
        
        do {
            dragon.x = rand.nextInt(GRID_WIDTH - 4) + 2;
            dragon.y = rand.nextInt(GRID_HEIGHT - 4) + 2;
            attempts++;
        } 
        while ((walls[dragon.x][dragon.y] || Math.abs(dragon.x - player.x) <= 2 || Math.abs(dragon.y - player.y) <= 2) && attempts < 100);
        
        if (attempts >= 100) {
            for (int x = 1; x < GRID_WIDTH - 1; x++) {
                for (int y = 1; y < GRID_HEIGHT - 1; y++) {
                    if (!walls[x][y] && Math.abs(x - player.x) > 2 && Math.abs(y - player.y) > 2) {
                        dragon.x = x;
                        dragon.y = y;
                        return;
                    }
                }
            }
        }
    }
    
    /*
    @param
    @return
    */
    private void startGame() {
        gameTimer = new Timer(200, this);
        gameTimer.start();
        
        clockTimer = new Timer(1000, e -> {
            if (gameRunning) {
                currentTime++;
                parent.updateTime(currentTime);
            }
        });
        
        clockTimer.start();
    }
    
    /*
    @param
    @return
    */
    private void checkGameConditions() {
        if (player.x == GRID_WIDTH - 2 && player.y == 1) {
            levelComplete();
            return;
        }
        
        if (Math.abs(player.x - dragon.x) <= 1 && Math.abs(player.y - dragon.y) <= 1) {
            gameOver();
        }
    }
    
    /*
    @param
    @return
    */
    private void levelComplete() {
        gameRunning = false;
        levelsCompleted++;
        parent.levelComplete(levelsCompleted, currentTime);
        nextLevel();
    }
    
    /*
    @param
    @return
    */
    private void gameOver() {
        gameRunning = false;
        clockTimer.stop();
        parent.gameOver(levelsCompleted);
    }
    
    /*
    @param
    @return
    */
    private void nextLevel() {
        loadRandomLevel();
        player.x = 1;
        player.y = GRID_HEIGHT - 2;
        repositionDragon();
        currentTime = 0;
        parent.updateTime(currentTime);
        parent.updateLevelsCompleted(levelsCompleted);
        gameRunning = true;
        repaint();
    }
    
    /*
    @param
    @return
    */
    public void restartGame() {
        if (gameTimer != null) gameTimer.stop();
        if (clockTimer != null) clockTimer.stop();
        levelManager.reset();
        gameRunning = true;
        initializeGame();
        startGame();
        repaint();
        requestFocus();
    }
    
    /*
    @param Graphics
    @return
    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gameRunning) return;
        
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                int distance = Math.max(Math.abs(x - player.x), Math.abs(y - player.y));
                
                if (distance <= VISIBILITY_RANGE) {
                    if (walls[x][y]) {
                        g.setColor(Color.GRAY);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    else {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    
                    g.setColor(Color.BLACK);
                    g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        
        g.setColor(Color.GREEN);
        g.fillRect((GRID_WIDTH - 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect((GRID_WIDTH - 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLUE);
        g.fillOval(player.x * TILE_SIZE, player.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        int dragonDistance = Math.max(Math.abs(dragon.x - player.x), Math.abs(dragon.y - player.y));
        
        if (dragonDistance <= VISIBILITY_RANGE) {
            g.setColor(Color.RED);
            g.fillRect(dragon.x * TILE_SIZE, dragon.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        
        g.setColor(Color.YELLOW);
        g.drawRect((player.x - VISIBILITY_RANGE) * TILE_SIZE, (player.y - VISIBILITY_RANGE) * TILE_SIZE, VISIBILITY_RANGE * 2 * TILE_SIZE + TILE_SIZE, VISIBILITY_RANGE * 2 * TILE_SIZE + TILE_SIZE);
    }
    
    /*
    @param ActionEvent
    @return
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameRunning) return;
        dragon.move(walls, GRID_WIDTH, GRID_HEIGHT);
        checkGameConditions();
        repaint();
    }
    
    /*
    @param KeyEvent
    @return
    */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameRunning) return;
        int key = e.getKeyCode();
        
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.move(0, -1, walls, GRID_WIDTH, GRID_HEIGHT);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                player.move(0, 1, walls, GRID_WIDTH, GRID_HEIGHT);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                player.move(-1, 0, walls, GRID_WIDTH, GRID_HEIGHT);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                player.move(1, 0, walls, GRID_WIDTH, GRID_HEIGHT);
                break;
        }
        
        checkGameConditions();
        repaint();
    }
    
    /*
    @param KeyEvent
    @return
    */
    @Override
    public void keyReleased(KeyEvent e) {}
    
    /*
    @param KeyEvent
    @return
    */
    @Override
    public void keyTyped(KeyEvent e) {}
}
