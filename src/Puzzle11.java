import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle11 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day6.txt"));
        char[] stream = lines.get(0).toCharArray();

        int count = 4;
        for (int i = 0; i < stream.length - 3; ++i) {
            if (stream[i] == stream[i + 1] || stream[i] == stream[i + 2] || stream[i] == stream[i + 3]
                    || stream[i + 1] == stream[i + 2] || stream[i + 1] == stream[i + 3] || stream[i + 2] == stream[i + 3]) {
                ++count;
            } else {
                break;
            }
        }

        return count;
    }

}
