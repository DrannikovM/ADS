import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    enum State {
        START,
        HAS_LESS,
        HAS_SIGN,
        IN_DIGITS,
        IN_LETTERS,
        ACCEPT,
        ERROR
    }
    public static void main(String[] args) {
        System.out.println("=== ЗАВДАННЯ 1 (Пошук регулярним виразом у файлі) ===");

        String regex = "^<(\\+|-)([0-5]+|[P-Z]+)>$";
        Pattern pattern = Pattern.compile(regex);

        try (BufferedReader br = new BufferedReader(new FileReader("words1.txt"))) {
            String line;
            System.out.println("Слова у файлі words1.txt, що відповідають умові:");
            while ((line = br.readLine()) != null) {
                line = line.trim();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    System.out.println("[Знайдено]: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу words1.txt. Створіть його у папці проєкту.");
        }

        System.out.println("\n=== ЗАВДАННЯ 2 (Аналізатор на основі Switch) ===");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть рядок для перевірки автоматом: ");
        String inputStr = scanner.nextLine().trim();

        if (validateWithSwitch(inputStr)) {
            System.out.println("Результат: Рядок ПРАВИЛЬНИЙ (Образ розпізнано).");
        } else {
            System.out.println("Результат: Рядок НЕПРАВИЛЬНИЙ.");
        }

        System.out.println("\n=== ЗАВДАННЯ 3 (Аналіз тексту з файлу за таблицею переходів) ===");

        Map<State, Map<String, State>> transitionTable = initTransitionTable();

        try (BufferedReader br = new BufferedReader(new FileReader("text3.txt"))) {
            String line;
            if ((line = br.readLine()) != null) {
                System.out.println("Зміст файлу text3.txt: " + line);

                String[] words = line.split("[()]+");

                System.out.println("\nРезультати аналізу слів:");
                for (String word : words) {
                    if (word.isEmpty()) continue;

                    boolean isValid = validateWithTable(word, transitionTable);
                    System.out.printf("Слово: %-15s -> %s\n", word, (isValid ? "ПРАВИЛЬНЕ" : "НЕПРАВИЛЬНЕ"));
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу text3.txt. Створіть його у папці проєкту.");
        }
    }
    public static boolean validateWithSwitch(String str) {
        State currentState = State.START;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            switch (currentState) {
                case START:
                    if (c == '<') currentState = State.HAS_LESS;
                    else currentState = State.ERROR;
                    break;

                case HAS_LESS:
                    if (c == '+' || c == '-') currentState = State.HAS_SIGN;
                    else currentState = State.ERROR;
                    break;

                case HAS_SIGN:
                    if (c >= '0' && c <= '5') currentState = State.IN_DIGITS;
                    else if (c >= 'P' && c <= 'Z') currentState = State.IN_LETTERS;
                    else currentState = State.ERROR;
                    break;

                case IN_DIGITS:
                    if (c >= '0' && c <= '5') currentState = State.IN_DIGITS;
                    else if (c == '>') currentState = State.ACCEPT;
                    else currentState = State.ERROR;
                    break;

                case IN_LETTERS:
                    if (c >= 'P' && c <= 'Z') currentState = State.IN_LETTERS;
                    else if (c == '>') currentState = State.ACCEPT;
                    else currentState = State.ERROR;
                    break;

                case ACCEPT:
                    currentState = State.ERROR;
                    break;

                case ERROR:
                    return false;
            }
        }
        return currentState == State.ACCEPT;
    }
    private static Map<State, Map<String, State>> initTransitionTable() {
        Map<State, Map<String, State>> table = new HashMap<>();

        for (State state : State.values()) {
            table.put(state, new HashMap<>());
        }

        table.get(State.START).put("<", State.HAS_LESS);

        table.get(State.HAS_LESS).put("+", State.HAS_SIGN);
        table.get(State.HAS_LESS).put("-", State.HAS_SIGN);

        table.get(State.HAS_SIGN).put("digit", State.IN_DIGITS);
        table.get(State.HAS_SIGN).put("letter", State.IN_LETTERS);

        table.get(State.IN_DIGITS).put("digit", State.IN_DIGITS);
        table.get(State.IN_DIGITS).put(">", State.ACCEPT);

        table.get(State.IN_LETTERS).put("letter", State.IN_LETTERS);
        table.get(State.IN_LETTERS).put(">", State.ACCEPT);

        return table;
    }
    private static String getCharClass(char c) {
        if (c == '<') return "<";
        if (c == '>') return ">";
        if (c == '+' || c == '-') return String.valueOf(c);
        if (c >= '0' && c <= '5') return "digit";
        if (c >= 'P' && c <= 'Z') return "letter";
        return "unknown";
    }

    public static boolean validateWithTable(String str, Map<State, Map<String, State>> table) {
        State currentState = State.START;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            String charClass = getCharClass(c);

            State nextState = table.get(currentState).get(charClass);

            if (nextState == null) {
                currentState = State.ERROR;
                break;
            }
            currentState = nextState;
        }

        return currentState == State.ACCEPT;
    }
}