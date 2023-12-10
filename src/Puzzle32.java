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
public class Puzzle32 {

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

        return maxFlow(new HashMap<>(), new State(26, start, 26, start, 0, 0), valves.size(), distance, valves);
    }

    long maxFlow(Map<State, Long> cache, State state, int numValves, int[][] distance, Map<Integer, Valve> valves) {
        if (cache.containsKey(state)) {
            return cache.get(state);
        }

        var options = new ArrayList<Long>();
        int lowMin = Math.min(state.min1(), state.min2());

        if (state.min1() > 0) {
            int bitmask = 1 << state.loc1();
            var valve = valves.get(state.loc1());

            if ((state.valves() & bitmask) == 0) {
                var newState = new State(state.min1() - 1, state.loc1(), state.min2(), state.loc2(), state.valves() | bitmask, state.flowRate() + valve.flowRate());
                options.add(maxFlow(cache, newState, numValves, distance, valves) + ((lowMin == state.min2()) ? state.flowRate() : 0));
            } else {
                var newState = new State(state.min1() - 1, state.loc1(), state.min2(), state.loc2(), state.valves(), state.flowRate());
                options.add(maxFlow(cache, newState, numValves, distance, valves) + ((lowMin == state.min2()) ? state.flowRate() : 0));
            }

            for (int i = 0; i < numValves; ++i) {
                if (i != state.loc1() && valves.get(i).flowRate() > 0) {
                    int time = distance[state.loc1()][i];
                    if (state.min1() - time > 0) {
                        var newState = new State(state.min1() - time, i, state.min2(), state.loc2(), state.valves(), state.flowRate());
                        if (lowMin == state.min1()) {
                            time = Math.max(time - state.min2() + state.min1(), 0);
                        }
                        options.add(maxFlow(cache, newState, numValves, distance, valves) + state.flowRate() * (long) time);
                    }
                }
            }
        }

        if (state.min2() > 0) {
            int bitmask = 1 << state.loc2();
            var valve = valves.get(state.loc2());

            if ((state.valves() & bitmask) == 0) {
                var newState = new State(state.min1(), state.loc1(), state.min2() - 1, state.loc2(), state.valves() | bitmask, state.flowRate() + valve.flowRate());
                options.add(maxFlow(cache, newState, numValves, distance, valves) + ((lowMin == state.min1()) ? state.flowRate() : 0));
            } else {
                var newState = new State(state.min1(), state.loc1(), state.min2() - 1, state.loc2(), state.valves(), state.flowRate());
                options.add(maxFlow(cache, newState, numValves, distance, valves) + ((lowMin == state.min1()) ? state.flowRate() : 0));
            }

            for (int i = 0; i < numValves; ++i) {
                if (i != state.loc2() && valves.get(i).flowRate() > 0) {
                    int time = distance[state.loc2()][i];
                    if (state.min2() - time > 0) {
                        var newState = new State(state.min1(), state.loc1(), state.min2() - time, i, state.valves(), state.flowRate());
                        if (lowMin == state.min2()) {
                            time = Math.max(time - state.min1() + state.min2(), 0);
                        }
                        options.add(maxFlow(cache, newState, numValves, distance, valves) + state.flowRate() * (long) time);
                    }
                }
            }
        }

        var best = options.stream().max(Comparator.comparingLong(n -> n)).orElse(0L);
        cache.put(state, best);

        return best;
    }

    record State(int min1, int loc1, int min2, int loc2, int valves, int flowRate) {
    }

    record Valve(int id, String label, int flowRate, Set<String> reachable) {
    }

}
