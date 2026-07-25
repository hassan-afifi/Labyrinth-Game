import java.sql.Timestamp;

 public class HighScore {
    private String playerName;
    private int score;
    private Timestamp timestamp;

    /*
    @param String, int, Timestamp
    @return
    */
    public HighScore(String playerName, int score, Timestamp timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = timestamp;
    }

    /*
    @param
    @return String
    */
    public String getPlayerName() { 
        return playerName; 
    }
    
    /*
    @param
    @return int
    */
    public int getScore() { 
        return score; 
    }
    
    /*
    @param
    @return Timestamp
    */
    public Timestamp getTimestamp() { 
        return timestamp; 
    }
}
