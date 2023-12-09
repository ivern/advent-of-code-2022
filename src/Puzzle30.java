import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Puzzle30 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day15.txt"));

        var regex = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
        var gaps = new ArrayList<Gap>();

        for (var line : lines) {
            var matcher = regex.matcher(line);
            if (!matcher.find()) {
                continue;
            }

            int sx = Integer.parseInt(matcher.group(1));
            int sy = Integer.parseInt(matcher.group(2));
            int bx = Integer.parseInt(matcher.group(3));
            int by = Integer.parseInt(matcher.group(4));

            int d = distance(sx, sy, bx, by);

            gaps.add(new Gap(sx, sy, d));
        }

        int min = 0;
        int max = 4_000_000;

        for (int y = min; y <= max; ++y) {
            var ends = new TreeMap<Integer, Integer>();

            for (var gap : gaps) {
                int dy = abs(y - gap.y());
                if (dy > gap.range()) {
                    continue;
                }

                ends.merge(gap.x() - gap.range() + dy, 1, Integer::sum);
                ends.merge(gap.x() + gap.range() - dy, -1, Integer::sum);
            }

            int counter = 0;
            int lastx = -1;
            for (var e : ends.entrySet()) {
                if (counter == 0 && e.getKey() > lastx + 1) {
                    return signalStrength(lastx + 1, y);
                }
                counter += e.getValue();
                lastx = e.getKey();
            }
        }

        return 0;
    }

    private long signalStrength(int x, int y) {
        return x * 4_000_000L + y;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return abs(x1 - x2) + abs(y1 - y2);
    }

    private record Gap(int x, int y, int range) {
    }

}
