import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Puzzle26 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day13.txt"));

        var pkt1 = parse("[[2]]");
        var pkt2 = parse("[[6]]");

        List<List<Object>> parsed = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 3) {
            parsed.add(parse(lines.get(i)));
            parsed.add(parse(lines.get(i + 1)));
        }
        parsed.add(pkt1);
        parsed.add(pkt2);

        parsed.sort((a, b) -> validate(b, a));

        int result = 1;

        for (int i = 0; i < parsed.size(); ++i) {
            if (parsed.get(i) == pkt1 || parsed.get(i) == pkt2) {
                result *= (i + 1);
            }
        }

        return result;
    }

    int validate(List<Object> left, List<Object> right) {
        for (int i = 0; i < left.size(); ++i) {
            if (i >= right.size()) {
                return -1;
            }

            int v;

            if (left.get(i) instanceof Integer n1 && right.get(i) instanceof Integer n2) {
                v = validate(n1, n2);
            } else if (left.get(i) instanceof Integer n) {
                v = validate(List.of(n), (List<Object>) right.get(i));
            } else if (right.get(i) instanceof Integer n) {
                v = validate((List<Object>) left.get(i), List.of(n));
            } else {
                v = validate((List<Object>) left.get(i), (List<Object>) right.get(i));
            }

            if (v != 0) {
                return v;
            }
        }

        if (left.size() < right.size()) {
            return 1;
        }

        return 0;
    }

    int validate(Integer left, Integer right) {
        return right.compareTo(left);
    }

    List<Object> parse(String list) {
        List<Object> result = new ArrayList<>();

        int p = 1;
        var state = ParserState.BASE;
        int start = -1;
        int openParens = 0;

        while (p < list.length()) {
            var c = list.charAt(p);

            switch (state) {
                case BASE:
                    if (Character.isDigit(c)) {
                        state = ParserState.NUMBER;
                        start = p;
                    } else if (c == '[') {
                        state = ParserState.LIST;
                        start = p;
                        openParens = 1;
                    }
                    break;
                case NUMBER:
                    if (c == ',' || c == ']') {
                        result.add(Integer.parseInt(list, start, p, 10));
                        state = ParserState.BASE;
                    }
                    break;
                case LIST:
                    if (c == '[') {
                        openParens++;
                    }
                    if (c == ']') {
                        if (openParens == 1) {
                            result.add(parse(list.substring(start, p + 1)));
                            state = ParserState.BASE;
                        } else {
                            openParens--;
                        }
                    }
                    break;
            }

            ++p;
        }

        return result;
    }

    enum ParserState {
        BASE,
        NUMBER,
        LIST
    }

}
