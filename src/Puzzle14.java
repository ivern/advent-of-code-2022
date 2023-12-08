import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Puzzle14 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day7.txt"));

        int lineNum = 0;
        List<Directory> directories = new ArrayList<>();
        Directory root = new Directory("/");
        Directory current = root;

        while (lineNum < lines.size()) {
            var line = lines.get(lineNum++);
            if (line.startsWith("$ cd")) {
                var target = line.substring("$ cd ".length());
                if (target.equals("/")) {
                    current = root;
                } else if (target.equals("..")) {
                    current = current.parent;
                } else {
                    if (!current.entries.containsKey(target)) {
                        var directory = new Directory(target);
                        current.add(directory);
                        directories.add(directory);
                    }
                    current = (Directory) current.entries.get(target);
                }
            } else if (line.startsWith("$ ls")) {
                while (lineNum < lines.size() && !lines.get(lineNum).startsWith("$")) {
                    String[] parts = lines.get(lineNum++).split(" ");
                    if (parts[0].trim().equals("dir")) {
                        if (!current.entries.containsKey(parts[1].trim())) {
                            var directory = new Directory(parts[1].trim());
                            current.add(directory);
                            directories.add(directory);
                        }
                    } else {
                        current.add(new File(parts[1].trim(), Integer.parseInt(parts[0].trim())));
                    }
                }
            }
        }

        int needed = 30_000_000 - (70_000_000 - root.size());
        if (needed <= 0) {
            return 0;
        }

        return directories.stream().mapToInt(Node::size).filter(n -> n >= needed).min().orElse(-1);
    }

    private abstract class Node {
        protected final String name;

        Node(String name) {
            this.name = name;
        }

        abstract int size();
    }

    private class Directory extends Node {
        Map<String, Node> entries;
        Directory parent;

        public Directory(String name) {
            super(name);
            this.entries = new HashMap<>();
        }

        @Override
        int size() {
            return entries.values().stream().mapToInt(Node::size).sum();
        }

        void add(Node n) {
            if (n instanceof Directory d) {
                d.parent = this;
            }
            entries.put(n.name, n);
        }
    }

    private class File extends Node {
        protected final int size;

        public File(String name, int size) {
            super(name);
            this.size = size;
        }

        @Override
        int size() {
            return size;
        }
    }

}
