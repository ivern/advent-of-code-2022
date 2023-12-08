import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Puzzle9 {

    public String solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day5.txt"));

        ArrayList<Deque<Character>> stacks = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            stacks.add(new LinkedList<>());
        }

        int lineNum = 0;
        while (lines.get(lineNum).trim().startsWith("[")) {
            var line = lines.get(lineNum++);
            for (int i = 0; i * 4 + 1 < line.length(); ++i) {
                char c = line.charAt(i * 4 + 1);
                if (c != ' ') {
                    stacks.get(i + 1).add(c);
                }
            }
        }
        lineNum += 2;

        while (lineNum < lines.size()) {
            var line = lines.get(lineNum++);
            var parts = line.split(" ");
            int count = Integer.parseInt(parts[1].trim());
            int from = Integer.parseInt(parts[3].trim());
            int to = Integer.parseInt(parts[5].trim());
            while (count-- > 0) {
                stacks.get(to).push(stacks.get(from).pop());
            }
        }

        return stacks.stream().filter(d -> !d.isEmpty()).map(d -> "" + d.peek()).collect(Collectors.joining());
    }

    private record Ranges(Range a, Range b) {
        static Ranges parse(String s) {
            String[] parts = s.split(",");
            return new Ranges(Range.parse(parts[0].trim()), Range.parse(parts[1].trim()));
        }

        int score() {
            return a.disjoint(b) ? 0 : 1;
        }
    }

    private record Range(int min, int max) {
        static Range parse(String s) {
            String[] parts = s.split("-");
            return new Range(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
        }

        boolean disjoint(Range r) {
            return max < r.min || r.max < min;
        }
    }

}
