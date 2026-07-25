import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class LabyrinthGame extends JFrame {
    private GamePanel gamePanel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private DatabaseManager dbManager;
    
    /*
    @param
    @return
    */
    public LabyrinthGame() {
        dbManager = new DatabaseManager();
        initializeUI();
        setupWindowListener();
    }
    
    /*
    @param
    @return
    */
    private void initializeUI() {
        setTitle("Labyrinth Escape");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        scoreLabel = new JLabel("Labyrinths Solved: 0", JLabel.CENTER);
        timeLabel = new JLabel("Time: 0s", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);
        gamePanel = new GamePanel(this);
        add(gamePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /*
    @param
    @return
    */
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }
    
    /*
    @param
    @return
    */
    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the game?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.OK_OPTION) {
            dbManager.close();
            System.exit(0);
        }
    }
    
    /*
    @param
    @return JMenuBar
    */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        restartItem.addActionListener(e -> restartGame());
        exitItem.addActionListener(e -> confirmExit());
        gameMenu.add(restartItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        JMenu scoreMenu = new JMenu("High Scores");
        JMenuItem showScoresItem = new JMenuItem("Show High Scores");
        showScoresItem.addActionListener(e -> showHighScores());
        scoreMenu.add(showScoresItem);
        menuBar.add(gameMenu);
        menuBar.add(scoreMenu);
        return menuBar;
    }
    
    /*
    @param int
    @return
    */
    public void updateTime(int seconds) {
        timeLabel.setText("Time: " + seconds + "s");
    }
    
    /*
    @param int
    @return
    */
    public void updateLevelsCompleted(int levels) {
        scoreLabel.setText("Labyrinths Solved: " + levels);
    }
    
    /*
    @param int, int
    @return
    */
    public void levelComplete(int levelsCompleted, int time) {        
        JOptionPane.showMessageDialog(this, "Level completed in " + time + " seconds!\nTotal labyrinths solved: " + levelsCompleted, "Level Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
    @param int
    @return
    */
    public void gameOver(int finalScore) {
        String name = JOptionPane.showInputDialog(this, "Game Over! You solved " + finalScore + " labyrinths.\nEnter your name:", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            
        if (name != null && !name.trim().isEmpty()) {
            dbManager.saveScore(name.trim(), finalScore);
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        }
        else {
            dbManager.close();
            System.exit(0);
        }
    }
    
    /*
    @param
    @return
    */
    private void restartGame() {
        scoreLabel.setText("Labyrinths Solved: 0");
        gamePanel.restartGame();
    }
    
    /*
    @param
    @return
    */
    private void showHighScores() {
        dbManager.showHighScores(this);
    }
    
    /*
    @param
    @return
    */
    @Override
    public void dispose() {
        dbManager.close();
        super.dispose();
    }
    
    /*
    @param String[]
    @return
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LabyrinthGame().setVisible(true);
        });
    }
}
