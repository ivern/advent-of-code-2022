import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.Math.max;

@SuppressWarnings("unused")
public class Puzzle28 {

    static final int HEIGHT = 400;
    static final int WIDTH = 1000;

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day14.txt"));

        int bottom = 0;
        char[][] cave = new char[HEIGHT][];
        for (int i = 0; i < HEIGHT; ++i) {
            cave[i] = new char[WIDTH];
            Arrays.fill(cave[i], '.');
        }

        for (var line : lines) {
            String[] parts = line.split(" -> ");
            int[] xs = new int[parts.length];
            int[] ys = new int[parts.length];

            for (int i = 0; i < parts.length; ++i) {
                String[] coords = parts[i].split(",");
                xs[i] = Integer.parseInt(coords[0]);
                ys[i] = Integer.parseInt(coords[1]);

                bottom = max(bottom, ys[i]);
            }

            for (int i = 0; i < parts.length - 1; ++i) {
                if (xs[i] == xs[i + 1]) {
                    if (ys[i] <= ys[i + 1]) {
                        for (int j = ys[i]; j <= ys[i + 1]; ++j) {
                            cave[j][xs[i]] = '#';
                        }
                    } else {
                        for (int j = ys[i + 1]; j <= ys[i]; ++j) {
                            cave[j][xs[i]] = '#';
                        }
                    }
                } else {
                    if (xs[i] <= xs[i + 1]) {
                        for (int j = xs[i]; j <= xs[i + 1]; ++j) {
                            cave[ys[i]][j] = '#';
                        }
                    } else {
                        for (int j = xs[i + 1]; j <= xs[i]; ++j) {
                            cave[ys[i]][j] = '#';
                        }
                    }
                }
            }
        }

        Arrays.fill(cave[2 + bottom], '#');

        int grains = 0;
        boolean stillGoing = true;

        while (stillGoing) {
            int x = 500;
            int y = 0;

            boolean dropping = true;
            while (dropping) {
                if (cave[y + 1][x] == '.') {
                    y++;
                } else if (x > 0 && cave[y + 1][x - 1] == '.') {
                    y++;
                    x--;
                } else if (x < WIDTH - 1 && cave[y + 1][x + 1] == '.') {
                    y++;
                    x++;
                } else {
                    grains++;
                    cave[y][x] = 'o';
                    dropping = false;

                    if (y == 0 && x == 500) {
                        stillGoing = false;
                    }
                }
            }
        }

        return grains;
    }

}
