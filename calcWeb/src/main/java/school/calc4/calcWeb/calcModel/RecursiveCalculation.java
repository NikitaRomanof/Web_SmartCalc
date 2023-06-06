package school.calc4.calcWeb.calcModel;

import jakarta.validation.constraints.NotNull;

import java.io.IOException;

import static java.lang.Math.*;

public class RecursiveCalculation {
    private final char[] strBuffer;
    private char curr_tok;
    private double number;

    private int step;

    public RecursiveCalculation(@NotNull String normalizedString) {
        strBuffer = normalizedString.toCharArray();
        step = 0;
        number = 0.0;
    }

    private void getToken() {
        if (step >= strBuffer.length) {
            curr_tok = 'Q';
            return;
        }
        char ch = strBuffer[step];
        ++step;
        switch (ch) {
            case '*', '/', '+', '-', 'w', '^', 'e', 'y', 'u', 'p', 'f', 'h', 'j', 'k', 'v', '(', ')' -> curr_tok = ch;
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> {
                StringBuilder buf = new StringBuilder(String.valueOf(ch));
                while (step < strBuffer.length && ((strBuffer[step] >= '0' && strBuffer[step] <= '9') || strBuffer[step] == '.')) {
                    ch = strBuffer[step];
                    ++step;
                    buf.append(ch);
                }
                number = Double.parseDouble(buf.toString());
                curr_tok = 'N';
            }
            default -> throw new IllegalArgumentException("Bad symbol");
        }
    }

    private double prim(boolean get) throws IOException {
        if (get) {
            getToken();
        }
        switch (curr_tok) {
            case 'N' -> {
                double tmp = number;
                getToken();
                return tmp;
            }
            case '-' -> {
                return -prim(true);
            }
            case '+' -> {
                return prim(true);
            }
            case 'e' -> {
                return sin(prim(true));
            }
            case 'y' -> {
                return cos(prim(true));
            }
            case 'u' -> {
                return tan(prim(true));
            }
            case 'f' -> {
                return atan(prim(true));
            }
            case 'h' -> {
                return asin(prim(true));
            }
            case 'j' -> {
                return acos(prim(true));
            }
            case 'p' -> {
                return sqrt(prim(true));
            }
            case 'v' -> {
                return log10(prim(true));
            }
            case 'k' -> {
                return log(prim(true));
            }
            case '(' -> {
                double e = expr(true);
                if (curr_tok != ')') {
                    throw new IllegalArgumentException("')' expected");
                }
                getToken();
                return e;
            }
            default -> throw new IllegalArgumentException("primary expected");
        }
    }

    private double pw(boolean get) throws IOException {
        double left = prim(get);

        while (true) {
            if (curr_tok == '^') {
                left = pow(left, prim(true));
            } else {
                return left;
            }
        }
    }

    private double term(boolean get) throws IOException {
        double left = pw(get);

        while (true) {
            switch (curr_tok) {
                case '*' -> left *= pw(true);
                case '/' -> {
                    double d = pw(true);
                    left /= d;
                    if (Math.abs(d) <= Math.abs(0.00000000000001)) throw new ArithmeticException("Divide by 0");
                }
                case 'w' -> {
                    double d = pw(true);
                    left = left % d;
                    if (Math.abs(d) <= Math.abs(0.00000000000001)) throw new ArithmeticException("Divide by 0");
                }
                default -> {
                    return left;
                }
            }
        }
    }

    private double expr(boolean get) throws IOException {
        double left = term(get);

        for (;;) {
            switch (curr_tok) {
                case '+' -> left += term(true);
                case '-' -> left -= term(true);
                default -> {
                    return left;
                }
            }
        }
    }

    public String calculation() throws IOException {
        double tmp = 0.0;
        while (step <= strBuffer.length) {
            getToken();
            if (curr_tok == 'Q') {
                break;
            }
            tmp = expr(false);
        }
        return String.valueOf(tmp);
    }

}
