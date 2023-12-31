import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle3 {

    public int solve() throws IOException{
        var lines = Files.readAllLines(Paths.get("./data/day2.txt"));
        return lines.stream().mapToInt(this::score).sum();
    }

    private int score(String game) {
        String[] moves = game.split(" ");
        String theirs = moves[0].trim();
        String mine = translate(moves[1]);

        int score = switch(mine) {
            case "A" -> 1;
            case "B" -> 2;
            case "C" -> 3;
            default -> throw new RuntimeException("bad move");
        };

        if (theirs.equals(mine)) {
            score += 3;
        } else if ((theirs.equals("A") && mine.equals("B"))
                || (theirs.equals("B") && mine.equals("C"))
                || (theirs.equals("C") && mine.equals("A"))) {
            score += 6;
        }

        return score;
    }

    private String translate(String s) {
        return switch(s.trim()) {
            case "X" -> "A";
            case "Y" -> "B";
            case "Z" -> "C";
            default -> throw new RuntimeException("bad input");
        };
    }

}
