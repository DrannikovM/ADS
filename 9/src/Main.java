import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static int task1Count = 0;
    private static int task2Count = 0;
    private static List<String> task1Results = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ЛАБОРАТОРНА РОБОТА 2.3 ===");

        System.out.println("\n[Завдання 1: Варіанти розкладу дисциплін]");
        System.out.print("Введіть загальну кількість дисциплін (n, рекомендовано 9): ");
        int n1 = scanner.nextInt();
        System.out.print("Введіть кількість дисциплін на день (k, рекомендовано 3): ");
        int k1 = scanner.nextInt();

        String[] subjects = new String[n1];
        for (int i = 0; i < n1; i++) {
            subjects[i] = "Дисц_" + (i + 1);
        }

        boolean[] used = new boolean[n1];
        generateArrangements(subjects, new String[k1], used, 0, k1);

        System.out.println("-> Математично розраховано варіантів: " + (factorial(n1) / factorial(n1 - k1)));
        System.out.println("-> Алгоритмічно згенеровано варіантів: " + task1Count);

        String fileName = "9/combinations.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String alignment : task1Results) {
                writer.write(alignment);
                writer.newLine();
            }
            System.out.println("-> [Завдання 3]: Усі варіанти успішно записано у файл '" + fileName + "'");
        } catch (IOException e) {
            System.out.println("Помилка запису у файл: " + e.getMessage());
        }

        System.out.println("\n[Завдання 2: Дев'ятизначні числа з повтореннями]");

        int count1 = 1 + 1;
        int count2 = 2 + 1;
        int count3 = 3 + 1;
        int totalLength = count1 + count2 + count3;

        int[] digits = new int[totalLength];
        int idx = 0;
        for (int i = 0; i < count1; i++) digits[idx++] = 1;
        for (int i = 0; i < count2; i++) digits[idx++] = 2;
        for (int i = 0; i < count3; i++) digits[idx++] = 3;

        System.out.println("Початковий набір цифр для перестановки: " + Arrays.toString(digits));

        Arrays.sort(digits);
        generateUniquePermutations(digits, 0);

        long mathPerm = factorial(totalLength) / (factorial(count1) * factorial(count2) * factorial(count3));
        System.out.println("-> Математично розраховано унікальних чисел: " + mathPerm);
        System.out.println("-> Алгоритмічно згенеровано унікальних чисел: " + task2Count);

        scanner.close();
    }

    private static void generateArrangements(String[] elements, String[] current, boolean[] used, int depth, int k) {
        if (depth == k) {
            task1Count++;
            task1Results.add(Arrays.toString(current));
            return;
        }

        for (int i = 0; i < elements.length; i++) {
            if (!used[i]) {
                used[i] = true;
                current[depth] = elements[i];
                generateArrangements(elements, current, used, depth + 1, k);
                used[i] = false;
            }
        }
    }

    private static void generateUniquePermutations(int[] arr, int start) {
        if (start == arr.length) {
            task2Count++;
            return;
        }

        for (int i = start; i < arr.length; i++) {
            if (shouldSwap(arr, start, i)) {
                swap(arr, start, i);
                generateUniquePermutations(arr, start + 1);
                swap(arr, start, i);
            }
        }
    }

    private static boolean shouldSwap(int[] arr, int start, int curr) {
        for (int i = start; i < curr; i++) {
            if (arr[i] == arr[curr]) {
                return false;
            }
        }
        return true;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static long factorial(int num) {
        long fact = 1;
        for (int i = 1; i <= num; i++) {
            fact *= i;
        }
        return fact;
    }
}