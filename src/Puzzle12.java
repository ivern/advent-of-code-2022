import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle12 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day6.txt"));
        char[] stream = lines.get(0).toCharArray();

        int count = 14;
        for (int i = 0; i < stream.length - 13; ++i) {
            boolean nope = false;
            for (int j = i; j < i + 14 && !nope; ++j) {
                for (int k = j + 1; k < i + 14 && !nope; ++k) {
                    if (stream[j] == stream[k]) {
                        nope = true;
                    }
                }
            }

            if (!nope) {
                break;
            }

            ++count;
        }

        return count;
    }

}
