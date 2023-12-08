import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Puzzle19 {

    public int solve() throws IOException {
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

        int signalStrength = 0;
        for (int i = 20; i < ts.size(); i += 40) {
            signalStrength += i * ts.get(i);
        }

        return signalStrength;
    }

}
