import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Puzzle16 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day8.txt"));

        int height = lines.size();
        int width = lines.get(0).length();
        int maxScore = 0;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < height; ++x) {
                int h = Character.getNumericValue(lines.get(y).charAt(x));

                int[] visible = new int[4];
                Arrays.fill(visible, 0);

                for (int i = y + 1; i < height; ++i) {
                    int h2 = Character.getNumericValue(lines.get(i).charAt(x));
                    ++visible[0];
                    if (h2 >= h) {
                        break;
                    }
                }

                for (int i = y - 1; i >= 0; --i) {
                    int h2 = Character.getNumericValue(lines.get(i).charAt(x));
                    ++visible[1];
                    if (h2 >= h) {
                        break;
                    }
                }

                for (int i = x + 1; i < width; ++i) {
                    int h2 = Character.getNumericValue(lines.get(y).charAt(i));
                    ++visible[2];
                    if (h2 >= h) {
                        break;
                    }
                }

                for (int i = x - 1; i >= 0; --i) {
                    int h2 = Character.getNumericValue(lines.get(y).charAt(i));
                    ++visible[3];
                    if (h2 >= h) {
                        break;
                    }
                }

                int score = visible[0] * visible[1] * visible[2] * visible[3];
                maxScore = Math.max(maxScore, score);
            }
        }

        return maxScore;
    }

}
