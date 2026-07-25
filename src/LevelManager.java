import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelManager {
    private static final String LEVELS_DIR = "levels/";
    private static final int TOTAL_LEVELS = 10;
    private Random random;
    private List<Integer> availableLevels;
    
    /*
    @param
    @return
    */
    public LevelManager() {
        random = new Random();
        availableLevels = new ArrayList<>();
        reset();
    }
    
    /*
    @param
    @return boolean[][]
    */
    public boolean[][] loadRandomLevel() throws IOException {
        if (availableLevels.isEmpty()) {
            reset();
        }
        
        int randomIndex = random.nextInt(availableLevels.size());
        int levelNum = availableLevels.remove(randomIndex);
        return loadLevel(levelNum);
    }
    
    /*
    @param int
    @return boolean[][]
    */
    private boolean[][] loadLevel(int levelNum) throws IOException {
        String filename = LEVELS_DIR + "level" + levelNum + ".txt";
        File file = new File(filename);
        
        if (!file.exists()) {
            throw new IOException("Level file not found: " + filename);
        }
        
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        
        int width = lines.get(0).length();
        int height = lines.size();
        boolean[][] walls = new boolean[width][height];
        
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                walls[x][y] = line.charAt(x) == '#';
            }
        }
     
        return walls;
    }
       
    /*
    @param
    @return
    */ 
    public void reset() {
        availableLevels.clear();
        
        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            availableLevels.add(i);
        }
    }
}
