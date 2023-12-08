import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Puzzle6 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day3.txt"));

        int sum = 0;
        for (int i = 0; i < lines.size(); i += 3) {
            sum += priority(
                    items(lines.get(i)).stream()
                            .filter(items(lines.get(i + 1))::contains)
                            .filter(items(lines.get(i + 2))::contains)
                            .findFirst()
                            .get());
        }

        return sum;
    }

    private Set<Character> items(String rucksack) {
        Set<Character> items = new HashSet<>();
        for (Character c : rucksack.toCharArray()) {
            items.add(c);
        }
        return items;
    }

    private int priority(Character item) {
        if (Character.isLowerCase(item)) {
            return item - 'a' + 1;
        } else {
            return item - 'A' + 27;
        }
    }

}
