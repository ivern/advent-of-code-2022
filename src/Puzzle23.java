import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class Puzzle23 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day12.txt"));

        int height = lines.size();
        int width = lines.get(0).length();
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};

        Position start = null;
        Position end = null;

        Map<Position, List<Position>> edges = new TreeMap<>(Comparator.comparingInt(Position::row).thenComparingInt(Position::col));

        for (int row = 0; row < height; ++row) {
            var line = lines.get(row);

            for (int col = 0; col < width; ++col) {
                var c = line.charAt(col);
                int h = height(c);
                var position = new Position(row, col);

                if (c == 'S') {
                    start = position;
                } else if (c == 'E') {
                    end = position;
                }

                for (int i = 0; i < 4; ++i) {
                    int row2 = row + dy[i];
                    int col2 = col + dx[i];

                    if (row2 >= 0 && row2 < height && col2 >= 0 && col2 < width) {
                        int h2 = height(lines.get(row2).charAt(col2));
                        if (h2 <= h + 1) {
                            edges.computeIfAbsent(position, _p -> new ArrayList<>()).add(new Position(row2, col2));
                        }
                    }
                }
            }
        }

        var path = dijkstra(edges, start, end);

        return path.size() - 1;
    }

    List<Position> dijkstra(Map<Position, List<Position>> edges, Position start, Position end) {
        var nodes = new HashMap<Position, Node>();
        for (var p : edges.keySet()) {
            nodes.put(p, new Node(p));
        }

        var fringe = new PriorityQueue<Node>(Comparator.comparingInt(n -> n.distance));
        fringe.add(nodes.get(start));

        while (!fringe.isEmpty()) {
            var u = fringe.poll();

            for (var p2 : edges.get(u.position)) {
                var v = nodes.get(p2);
                int distance = u.distance + 1;
                if (distance < v.distance) {
                    fringe.remove(v);
                    v.distance = distance;
                    v.previous = u.position;
                    fringe.add(v);
                }
            }
        }

        var path = new ArrayList<Position>();

        Position current = end;
        while (current != start) {
            path.add(current);
            current = nodes.get(current).previous;
        }
        if (start != end) {
            path.add(start);
        }

        return path.reversed();
    }

    int height(Character c) {
        if (c == 'S') {
            return 0;
        } else if (c == 'E') {
            return 'z' - 'a';
        } else {
            return c - 'a';
        }
    }

    private record Position(int row, int col) {
    }

    private static class Node {
        Position position;
        int distance;
        Position previous;

        Node(Position position) {
            this.position = position;
            this.distance = Integer.MAX_VALUE;
            this.previous = null;
        }
    }

}
