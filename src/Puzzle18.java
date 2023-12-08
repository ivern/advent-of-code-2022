import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Puzzle18 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day9.txt"));

        Position[] knots = new Position[10];
        for (int i = 0; i < 10; ++i) {
            knots[i] = new Position(0, 0);
        }

        Set<Position> seen = new HashSet<>();
        seen.add(knots[knots.length - 1]);

        for (var line : lines) {
            String[] parts = line.split(" ");
            String direction = parts[0].trim();
            int distance = Integer.parseInt(parts[1].trim());

            while (distance-- > 0) {
                knots[0] = switch (direction) {
                    case "U" -> new Position(knots[0].x, knots[0].y + 1);
                    case "D" -> new Position(knots[0].x, knots[0].y - 1);
                    case "R" -> new Position(knots[0].x + 1, knots[0].y);
                    case "L" -> new Position(knots[0].x - 1, knots[0].y);
                    default -> throw new RuntimeException("bad move");
                };

                for (int i = 1; i < knots.length && !knots[i].touches(knots[i - 1]); ++i) {
                    knots[i] = knots[i].moveTowards(knots[i - 1]);
                }

                seen.add(knots[knots.length - 1]);
            }
        }

        return seen.size();
    }

    private record Position(int x, int y) {
        boolean touches(Position p) {
            return abs(x - p.x) <= 1 && abs(y - p.y) <= 1;
        }

        Position moveTowards(Position p) {
            if (p.x == x + 2 && p.y == y) {
                return new Position(x + 1, y);
            } else if (p.x == x - 2 && p.y == y) {
                return new Position(x - 1, y);
            } else if (p.y == y + 2 && p.x == x) {
                return new Position(x, y + 1);
            } else if (p.y == y - 2 && p.x == x) {
                return new Position(x, y - 1);
            } else {
                if (p.x < x && p.y < y) {
                    return new Position(x - 1, y - 1);
                } else if (p.x > x && p.y < y) {
                    return new Position(x + 1, y - 1);
                } else if (p.x < x && p.y > y) {
                    return new Position(x - 1, y + 1);
                } else if (p.x > x && p.y > y) {
                    return new Position(x + 1, y + 1);
                }
            }

            throw new RuntimeException("Cannot move " + this + " towards " + p);
        }
    }

}
