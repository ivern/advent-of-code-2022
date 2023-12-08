import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Puzzle20 {

    public String solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day10.txt"));

        int register = 1;
        var ts = new ArrayList<Integer>();
        ts.add(register);

        for (var line : lines) {
            if ("noop".equals(line.trim())) {
                ts.add(register);
            } else {
                int v = Integer.parseInt(line.substring("addx ".length()).trim());
                ts.add(register);
                ts.add(register);
                register += v;
            }
        }
        ts.add(register);

        StringBuilder result = new StringBuilder();
        result.append("\n");

        for (int y = 0; y < 6; ++y) {
            for (int x = 0; x < 40; ++x) {
                int tick = y * 40 + x + 1;
                result.append(abs(ts.get(tick) - x) <= 1 ? '#' : '.');
            }
            result.append("\n");
        }

        return result.toString();
    }

}
