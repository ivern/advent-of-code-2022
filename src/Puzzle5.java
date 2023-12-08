import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Puzzle5 {

    public int solve() throws IOException{
        var lines = Files.readAllLines(Paths.get("./data/day3.txt"));
        return lines.stream().mapToInt(this::priorities).sum();
    }

    private int priorities(String rucksack) {
        String leftStr = rucksack.substring(0, rucksack.length() / 2);
        Set<Character> left = new HashSet<>();
        for (Character c : leftStr.toCharArray()) {
            left.add(c);
        }

        String rightStr = rucksack.substring(rucksack.length() / 2);
        Set<Character> right = new HashSet<>();
        for (Character c : rightStr.toCharArray()) {
            right.add(c);
        }

        List<Character> inBoth = left.stream().filter(right::contains).toList();
        List<Integer> priorities = inBoth.stream().map(this::priority).toList();

        return priorities.stream().mapToInt(n -> n).sum();
    }

    private int priority(Character item) {
        if (Character.isLowerCase(item)) {
            return item - 'a' + 1;
        } else {
            return item - 'A' + 27;
        }
    }

}
