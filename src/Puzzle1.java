import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Integer.max;

@SuppressWarnings("unused")
public class Puzzle1 {

    public int solve() throws IOException{
        var lines = Files.readAllLines(Paths.get("./data/day1.txt"));

        int i = 0;
        int max = -1;

        while (i < lines.size()) {
            String line;
            int calories = 0;
            while (i < lines.size() && !(line = lines.get(i++)).isBlank()) {
                calories += Integer.parseInt(line.trim());
            }
            max = max(max, calories);
        }

        return max;
    }

}
