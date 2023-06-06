package school.calc4.calcWeb.calcModel;

import jakarta.validation.constraints.NotNull;

public class CalculatorModel {
    private final String inputString;

    public CalculatorModel(@NotNull String inputString) {
        this.inputString = inputString.toLowerCase().trim();
    }

    private void checkString(int countCheck, @NotNull String inStr) {
        String checkBadString = "wyupfhjkv";
        String checkGoodString = "weyupfhjkv+-*/m^()0123456789.";
        char[] bufStringArray = inStr.toCharArray();
        for (char c : bufStringArray) {
            if (countCheck == 1) {
                boolean containsBad = checkBadString.contains(String.valueOf(c));
                if (containsBad) {
                    throw new IllegalArgumentException("Bad symbol");
                }
            } else if (countCheck == 2) {
                boolean containsGood = checkGoodString.contains(String.valueOf(c));
                if (!containsGood) {
                    throw new IllegalArgumentException("Bad symbol");
                }
            }
        }
    }

    private void checkOpenCloseBrackets(@NotNull String finishString) {
        char[] bufStringArray = finishString.toCharArray();
        char prevChar = bufStringArray[0];
        if (prevChar == ')') throw new IllegalArgumentException("Invalid symbol after bracket");

        for (int i = 1; i < bufStringArray.length; ++i) {
            if (bufStringArray[i] == ')') {
                if (!((prevChar >= '0' && prevChar <= '9') || (prevChar == ')'))) {
                    throw new IllegalArgumentException("Invalid symbol before  close bracket");
                }
            } else if (bufStringArray[i] == '(') {
                if ((prevChar >= '0' && prevChar <= '9') || prevChar == ')') {
                    throw new IllegalArgumentException("Invalid symbol before open bracket");
                }
            }
            if (prevChar == ')') {
                if ((bufStringArray[i] >= '0' && bufStringArray[i] <= '9') || bufStringArray[i] == '(') {
                    throw new IllegalArgumentException("Invalid symbol after close bracket");
                }
            } else if (prevChar == '(') {
                if (bufStringArray[i] == '*' || bufStringArray[i] == '/' || bufStringArray[i] == '^'
                        || bufStringArray[i] == 'w' || bufStringArray[i] == ')') {
                    throw new IllegalArgumentException("Invalid symbol after open bracket");
                }
            }

            prevChar = bufStringArray[i];
        }
    }

    private void checkBracketsAndDots(@NotNull String checkString) {
        if (checkString.length() > 255) throw new IllegalArgumentException("String overflow");
        char[] bufStringArray = checkString.toCharArray();
        int countBracketsOpen = 0;
        int countBracketsClose = 0;
        for (int i = 0; i < bufStringArray.length;) {
            int countDot = 0;
            while (bufStringArray[i] >= '0' && bufStringArray[i] <= '9' || bufStringArray[i] == '.') {
                ++i;
                if (i == bufStringArray.length) break;
                if (bufStringArray[i] == '.') {
                    ++countDot;
                }
            }
            if (countDot > 1) throw new IllegalArgumentException("Invalid string");
            if (i < bufStringArray.length && bufStringArray[i] == '(') {
                ++countBracketsOpen;
            }
            if (i < bufStringArray.length && bufStringArray[i] == ')') {
                ++countBracketsClose;
            }
            ++i;
        }
        if (countBracketsOpen != countBracketsClose) {
            throw new IllegalArgumentException("Bad brackets");
        }
    }

    private @NotNull String rebuildString() {
        checkBracketsAndDots(inputString);
        checkString(1, inputString);
        String buffString = inputString.replaceAll("mod", "w");
        if (buffString.contains("atan")) {
            buffString = buffString.replaceAll("atan", "f");
        }
        if (buffString.contains("asin")) {
            buffString = buffString.replaceAll("asin", "h");
        }
        if (buffString.contains("acos")) {
            buffString = buffString.replaceAll("acos", "j");
        }
        if (buffString.contains("sin")) {
            buffString = buffString.replaceAll("sin", "e");
        }
        if (buffString.contains("sqrt")) {
            buffString = buffString.replaceAll("sqrt", "p");
        }
        if (buffString.contains("cos")) {
            buffString = buffString.replaceAll("cos", "y");
        }
        if (buffString.contains("tan")) {
            buffString = buffString.replaceAll("tan", "u");
        }
        if (buffString.contains("log")) {
            buffString = buffString.replaceAll("log", "v");
        }
        if (buffString.contains("ln")) {
            buffString = buffString.replaceAll("ln", "k");
        }
        checkString(2, buffString);
        return buffString;
    }

    public String calculateResult()  {
        String normalizedString;
        try {
            normalizedString = rebuildString();
            checkOpenCloseBrackets(normalizedString);
            RecursiveCalculation calc = new RecursiveCalculation(normalizedString);
            normalizedString = calc.calculation();
        } catch (Exception e) {
            normalizedString ="ERROR " + e.getMessage();
        }

        if (normalizedString.equals("-0.0")) normalizedString = "0.0";
        return normalizedString;
    }
}
