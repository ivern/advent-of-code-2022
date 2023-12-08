import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
public class Puzzle2 {

    public int solve() throws IOException{
        var lines = Files.readAllLines(Paths.get("./data/day1.txt"));

        int i = 0;
        List<Integer> elfCalories = new ArrayList<>();

        while (i < lines.size()) {
            String line;
            int calories = 0;
            while (i < lines.size() && !(line = lines.get(i++)).isBlank()) {
                calories += Integer.parseInt(line.trim());
            }
            elfCalories.add(calories);
        }

        elfCalories.sort(Comparator.comparingInt(s -> (int) s).reversed());

        return elfCalories.get(0) + elfCalories.get(1) + elfCalories.get(2);
    }

}
