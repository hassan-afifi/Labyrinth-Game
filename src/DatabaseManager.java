import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/Highscores";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private Connection connection;

    /*
    @param
    @return
    */
    public DatabaseManager() {
        initializeDatabase();
    }
    
    /*
    @param
    @return
    */
    private void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to MySQL database successfully!");
        } 
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. Please add the MySQL connector JAR to your classpath.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage() + "\nPlease ensure MySQL is running and the database exists.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*
    @param String, int
    @return
    */
    public void saveScore(String name, int score) {
        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Database not available. Score not saved.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql = "INSERT INTO highscores (timestamp, name, score) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, name);
            pstmt.setInt(3, score);
            pstmt.executeUpdate();
            maintainTopScores();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Failed to save score: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*
    @param
    @return
    */
    private void maintainTopScores() throws SQLException {
        String deleteSQL = "DELETE FROM highscores WHERE score NOT IN (SELECT score FROM (SELECT score FROM highscores ORDER BY score DESC LIMIT 10) AS top_scores)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }
    
    /*
    @param
    @return List<HighScore>
    */
    public List<HighScore> getTopScores() {
        List<HighScore> scores = new ArrayList<>();
        String sql = "SELECT name, score, timestamp FROM highscores ORDER BY score DESC, timestamp ASC LIMIT 10";

        if (connection == null) {
            return scores;
        }
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String playerName = rs.getString("name");
                int score = rs.getInt("score");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                scores.add(new HighScore(playerName, score, timestamp));
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load high scores: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return scores;
    }
    
    /*
    @param JFrame
    @return
    */
    public void showHighScores(JFrame parent) {
        List<HighScore> scores = getTopScores();
        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP 10 HIGH SCORES ===\n\n");
        
        if (scores.isEmpty()) {
            sb.append("No high scores yet!\n");
            sb.append("Play the game to set a new record!");
        }
        else {
            int rank = 1;
            
            for (HighScore score : scores) {
                String formattedDate = score.getTimestamp().toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                sb.append(String.format("%2d. %-15s %5d pts   %s\n", rank, score.getPlayerName(), score.getScore(), formattedDate));
                rank++;
            }
        }
        
        JOptionPane.showMessageDialog(parent, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
    @param
    @return
    */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            }
            catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
