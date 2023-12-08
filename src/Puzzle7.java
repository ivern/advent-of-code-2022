import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle7 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day4.txt"));
        return lines.stream().map(Ranges::parse).mapToInt(Ranges::score).sum();
    }

    private record Ranges(Range a, Range b) {
        static Ranges parse(String s) {
            String[] parts = s.split(",");
            return new Ranges(Range.parse(parts[0].trim()), Range.parse(parts[1].trim()));
        }

        int score() {
            return (a.fullyContains(b) || b.fullyContains(a)) ? 1 : 0;
        }
    }

    private record Range(int min, int max) {
        static Range parse(String s) {
            String[] parts = s.split("-");
            return new Range(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
        }

        boolean fullyContains(Range r) {
            return min() <= r.min() && r.max() <= max();
        }
    }

}
