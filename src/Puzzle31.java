import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Puzzle31 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day16.txt"));
        var regex = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); (.*)");

        int numValves = 0;
        var valves = new HashMap<Integer, Valve>();
        int start = 0;

        for (var line : lines) {
            Matcher matcher = regex.matcher(line);
            if (!matcher.find()) {
                continue;
            }

            String label = matcher.group(1);
            int flowRate = Integer.parseInt(matcher.group(2));

            String suffix = matcher.group(3).startsWith("tunnels lead to valves ")
                    ? matcher.group(3).substring("tunnels lead to valves ".length())
                    : matcher.group(3).substring("tunnel leads to valve ".length());
            Set<String> reachable = Arrays.stream(suffix.split(", ")).collect(Collectors.toSet());

            var valve = new Valve(numValves++, label, flowRate, reachable);

            valves.put(valve.id(), valve);
            if (label.equals("AA")) {
                start = valve.id();
            }
        }

        int[][] distance = new int[numValves][];
        for (int i = 0; i < numValves; ++i) {
            distance[i] = new int[numValves];
            for (int j = 0; j < numValves; ++j) {
                if (i == j) {
                    distance[i][j] = 0;
                } else {
                    distance[i][j] = (valves.get(i).reachable().contains(valves.get(j).label())) ? 1 : 10_000;
                }
            }
        }

        for (int k = 0; k < numValves; ++k) {
            for (int i = 0; i < numValves; ++i) {
                for (int j = 0; j < numValves; ++j) {
                    if (distance[i][j] > distance[i][k] + distance[k][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        return maxFlow(new HashMap<>(), new State(30, start, 0, 0), valves.size(), distance, valves);
    }

    long maxFlow(Map<State, Long> cache, State state, int numValves, int[][] distance, Map<Integer, Valve> valves) {
        if (cache.containsKey(state)) {
            return cache.get(state);
        } else if (state.minutes() < 1) {
            return 0;
        }

        var options = new ArrayList<Long>();
        int bitmask = 1 << state.location();
        var valve = valves.get(state.location());

        if ((state.valves() & bitmask) == 0) {
            var newState = new State(state.minutes() - 1, state.location(), state.valves() | bitmask, state.flowRate() + valve.flowRate());
            options.add(maxFlow(cache, newState, numValves, distance, valves) + state.flowRate());
        } else {
            var newState = new State(state.minutes() - 1, state.location(), state.valves(), state.flowRate());
            options.add(maxFlow(cache, newState, numValves, distance, valves) + state.flowRate());
        }

        for (int i = 0; i < numValves; ++i) {
            if (i != state.location() && valves.get(i).flowRate() > 0) {
                int time = distance[state.location()][i];
                if (state.minutes() - time > 0) {
                    var newState = new State(state.minutes() - time, i, state.valves(), state.flowRate());
                    options.add(maxFlow(cache, newState, numValves, distance, valves) + state.flowRate() * (long) time);
                }
            }
        }

        var best = options.stream().max(Comparator.comparingLong(n -> n)).orElse(0L);
        cache.put(state, best);

        return best;
    }

    record State(int minutes, int location, int valves, int flowRate) {
    }

    record Valve(int id, String label, int flowRate, Set<String> reachable) {
    }

}
