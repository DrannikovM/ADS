import java.util.Random;

public class Main {
    public static void shellSortClassic(double[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                double key = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > key) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = key;
            }
        }
    }
    public static void shellSortKnuth(double[] arr) {
        int n = arr.length;
        int gap = 1;

        // Знаходимо максимальний початковий крок за Кнутом
        while (gap < n / 3) {
            gap = gap * 3 + 1;
        }

        for (; gap > 0; gap /= 3) {
            for (int i = gap; i < n; i++) {
                double key = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > key) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = key;
            }
        }
    }
    public static double[] generateRandomArray(int size) {
        Random rand = new Random();
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextDouble() * 10000;
        }
        return arr;
    }

    public static double[] generateSortedArray(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i; // Впорядкований масив (найкращий випадок)
        }
        return arr;
    }

    public static double[] generateReversedArray(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i; // Зворотний масив (найгірший випадок)
        }
        return arr;
    }
    public static void main(String[] args) {
        int[] sizes = {100, 10000, 1000000};

        System.out.println("================================================================");
        System.out.println(" ЗАВДАННЯ 1 та 2: ПОРІВНЯННЯ ШЕЛЛА (КЛАСИЧНИЙ) ТА ШЕЛЛА (КНУТ)");
        System.out.println("================================================================");
        System.out.printf("%-10s | %-22s | %-22s\n", "Розмір (N)", "Шелл Класичний (нс)", "Шелл Кнут (нс)");
        System.out.println("----------------------------------------------------------------");

        for (int size : sizes) {
            // Набір даних має бути абсолютно однаковим для обох алгоритмів
            double[] originalArray = generateRandomArray(size);
            double[] arrClassic = originalArray.clone();
            double[] arrKnuth = originalArray.clone();

            // Замір для класичного Шелла
            long startTime = System.nanoTime();
            shellSortClassic(arrClassic);
            long endTime = System.nanoTime();
            long durationClassic = endTime - startTime;

            // Замір для Шелла з кроком Кнута
            startTime = System.nanoTime();
            shellSortKnuth(arrKnuth);
            endTime = System.nanoTime();
            long durationKnuth = endTime - startTime;

            System.out.printf("%-10d | %-22d | %-22d\n", size, durationClassic, durationKnuth);
        }

        System.out.println("\n================================================================");
        System.out.println(" ЗАВДАННЯ 3: ВПЛИВ СТРУКТУРОВАНОСТІ ДАНИХ (N = 10000)");
        System.out.println("================================================================");
        System.out.printf("%-20s | %-22s | %-22s\n", "Тип масиву", "Шелл Класичний (нс)", "Шелл Кнут (нс)");
        System.out.println("----------------------------------------------------------------");

        int size3 = 10000;

        double[] sortedClassic = generateSortedArray(size3);
        double[] sortedKnuth = sortedClassic.clone();

        long start = System.nanoTime();
        shellSortClassic(sortedClassic);
        long timeSortedClassic = System.nanoTime() - start;

        start = System.nanoTime();
        shellSortKnuth(sortedKnuth);
        long timeSortedKnuth = System.nanoTime() - start;
        System.out.printf("%-20s | %-22d | %-22d\n", "Впорядкований (Мін)", timeSortedClassic, timeSortedKnuth);

        double[] randomClassic = generateRandomArray(size3);
        double[] randomKnuth = randomClassic.clone();

        start = System.nanoTime();
        shellSortClassic(randomClassic);
        long timeRandomClassic = System.nanoTime() - start;

        start = System.nanoTime();
        shellSortKnuth(randomKnuth);
        long timeRandomKnuth = System.nanoTime() - start;
        System.out.printf("%-20s | %-22d | %-22d\n", "Випадковий (Серед)", timeRandomClassic, timeRandomKnuth);

        double[] reversedClassic = generateReversedArray(size3);
        double[] reversedKnuth = reversedClassic.clone();

        start = System.nanoTime();
        shellSortClassic(reversedClassic);
        long timeReversedClassic = System.nanoTime() - start;

        start = System.nanoTime();
        shellSortKnuth(reversedKnuth);
        long timeReversedKnuth = System.nanoTime() - start;
        System.out.printf("%-20s | %-22d | %-22d\n", "Зворотний (Макс)", timeReversedClassic, timeReversedKnuth);
    }
}