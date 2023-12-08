import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle15 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day8.txt"));

        int height = lines.size();
        int width = lines.get(0).length();
        int numVisible = 0;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < height; ++x) {
                boolean visible = false;
                int h = Character.getNumericValue(lines.get(y).charAt(x));

                boolean maybeVisible = true;
                for (int i = y + 1; i < height; ++i) {
                    int h2 = Character.getNumericValue(lines.get(i).charAt(x));
                    if (h2 >= h) {
                        maybeVisible = false;
                        break;
                    }
                }
                if (maybeVisible) {
                    visible = true;
                }

                maybeVisible = true;
                for (int i = 0; i < y && !visible; ++i) {
                    int h2 = Character.getNumericValue(lines.get(i).charAt(x));
                    if (h2 >= h) {
                        maybeVisible = false;
                        break;
                    }
                }
                if (maybeVisible) {
                    visible = true;
                }

                maybeVisible = true;
                for (int i = x + 1; i < width && !visible; ++i) {
                    int h2 = Character.getNumericValue(lines.get(y).charAt(i));
                    if (h2 >= h) {
                        maybeVisible = false;
                        break;
                    }
                }
                if (maybeVisible) {
                    visible = true;
                }

                maybeVisible = true;
                for (int i = 0; i < x && !visible; ++i) {
                    int h2 = Character.getNumericValue(lines.get(y).charAt(i));
                    if (h2 >= h) {
                        maybeVisible = false;
                        break;
                    }
                }
                if (maybeVisible) {
                    visible = true;
                }

                if (visible) {
                    ++numVisible;
                }
            }
        }

        return numVisible;
    }

}
