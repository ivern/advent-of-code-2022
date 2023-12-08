import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Puzzle17 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day9.txt"));

        Position head = new Position(0, 0);
        Position tail = new Position(0, 0);

        Set<Position> seen = new HashSet<>();
        seen.add(tail);

        for (var line : lines) {
            String[] parts = line.split(" ");
            String direction = parts[0].trim();
            int distance = Integer.parseInt(parts[1].trim());

            while (distance-- > 0) {
                head = switch (direction) {
                    case "U" -> new Position(head.x, head.y + 1);
                    case "D" -> new Position(head.x, head.y - 1);
                    case "R" -> new Position(head.x + 1, head.y);
                    case "L" -> new Position(head.x - 1, head.y);
                    default -> throw new RuntimeException("bad move");
                };

                if (!head.touches(tail)) {
                    tail = tail.moveTowards(head);
                    seen.add(tail);
                }
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
