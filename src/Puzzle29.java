import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@SuppressWarnings("unused")
public class Puzzle29 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day15.txt"));

        var regex = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
        var gaps = new ArrayList<Gap>();
        var beacons = new HashSet<Pos>();
        int xmin = Integer.MAX_VALUE;
        int xmax = Integer.MIN_VALUE;

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
            beacons.add(new Pos(bx, by));

            xmin = min(xmin, sx - d);
            xmax = max(xmax, sx + d);
        }

        int count = 0;
        final int y = 2000000;

        for (int x = xmin; x <= xmax; ++x) {
            boolean empty = false;

            for (var gap : gaps) {
                if (distance(x, y, gap.x(), gap.y()) <= gap.range() && !beacons.contains(new Pos(x, y))) {
                    empty = true;
                    break;
                }
            }

            if (empty) {
                ++count;
            }
        }

        return count;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return abs(x1 - x2) + abs(y1 - y2);
    }

    private record Gap(int x, int y, int range) {
    }

    private record Pos(int x, int y) {
    }

}
