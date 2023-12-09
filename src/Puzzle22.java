import util.MathUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Puzzle22 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day11.txt"));

        Map<Integer, Monkey> monkeysById = new HashMap<>();
        Map<Integer, Long> inspections = new HashMap<>();
        int lcm = 1;

        for (int i = 0; i < lines.size(); ++i) {
            var line = lines.get(i).trim();
            if (line.isBlank()) {
                continue;
            }

            int id = Integer.parseInt(line.substring("Monkey ".length(), line.length() - 1));

            line = lines.get(++i);
            var items = Arrays.stream(line.substring("  Starting items: ".length()).split(", "))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toList();
            items = new ArrayList<>(items);

            line = lines.get(++i);
            var operation = line.charAt("  Operation: new = old ".length());
            var operand = line.substring("  Operation: new = old + ".length()).trim();
            Function<Long, Long> worryFunction = n -> {
                long o = operand.equals("old") ? n : Long.parseLong(operand);
                return (operation == '+') ? n + o : n * o;
            };

            line = lines.get(++i);
            var testParameter = Integer.parseInt(line.substring("  Test: divisible by ".length()).trim());
            lcm = MathUtil.lcm(lcm, testParameter);

            line = lines.get(++i);
            var passMonkey = Integer.parseInt(line.substring("    If true: throw to monkey ".length()).trim());

            line = lines.get(++i);
            var failMonkey = Integer.parseInt(line.substring("    If false: throw to monkey ".length()).trim());

            var monkey = new Monkey(id, items, worryFunction, n -> n % testParameter == 0, passMonkey, failMonkey);

            monkeysById.put(id, monkey);
            inspections.put(id, 0L);
        }

        for (int round = 0; round < 10_000; ++round) {
            for (Monkey monkey : monkeysById.values()) {
                for (Long worry : monkey.items()) {
                    inspections.merge(monkey.id, 1L, Long::sum);
                    worry = monkey.worryFunction().apply(worry) % lcm;
                    int target = monkey.test().test(worry) ? monkey.passMonkey() : monkey.failMonkey();
                    monkeysById.get(target).items().add(worry);
                }
                monkey.items().clear();
            }
        }

        var sortedInspections = inspections.values().stream().sorted(Comparator.comparingLong(n -> (long) n).reversed()).toList();
        return sortedInspections.get(0) * sortedInspections.get(1);
    }

    private record Monkey(
            int id, List<Long> items, Function<Long, Long> worryFunction, Predicate<Long> test,
            int passMonkey, int failMonkey) {
    }

}
