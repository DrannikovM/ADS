import java.util.Scanner;

public class Main {
    public static double f1(double x) {
        return 0.5 * Math.exp(Math.sqrt(1 + 2 * x));
    }
    public static void runTask1(double a, double b, double h) {
        int n = (int) Math.round((b - a) / h);

        double sumRect = 0;
        for (int i = 0; i < n; i++) {
            sumRect += f1(a + h * (i + 0.5));
        }
        double resRect = sumRect * h;

        double sumTrap = 0.5 * (f1(a) + f1(b));
        for (int i = 1; i < n; i++) {
            sumTrap += f1(a + i * h);
        }
        double resTrap = sumTrap * h;

        double resSimp = f1(a) + f1(b);
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            if (i % 2 == 0) {
                resSimp += 2 * f1(x);
            } else {
                resSimp += 4 * f1(x);
            }
        }
        resSimp = (resSimp * h) / 3.0;

        System.out.println("\n--- Результати Завдання 1 ---");
        System.out.printf("Метод прямокутників: %.6f\n", resRect);
        System.out.printf("Метод трапецій:      %.6f\n", resTrap);
        System.out.printf("Метод Сімпсона:      %.6f\n", resSimp);
    }
    public static double f2(double x) {
        return Math.pow(x, 4) + 6 * Math.pow(x, 2) - 12 * x - 8;
    }
    public static double df2(double x) {
        return 4 * Math.pow(x, 3) + 12 * x - 12;
    }
    public static double dichotomyMethod(double a, double b, double eps) {
        double k = a;
        while ((b - a) > eps) {
            k = (a + b) / 2;
            if (Math.abs(f2(k)) < 1e-12) return k;
            if (f2(a) * f2(k) < 0) b = k;
            else a = k;
        }
        return k;
    }
    public static double newtonMethod(double x0, double eps) {
        double x1 = x0;
        for (int i = 0; i < 1000; i++) {
            double df = df2(x1);
            if (Math.abs(df) < 1e-12) break; // Захист від ділення на 0
            double xNext = x1 - f2(x1) / df;
            if (Math.abs(xNext - x1) < eps) return xNext;
            x1 = xNext;
        }
        return x1;
    }
    public static double secantMethod(double a, double b, double eps) {
        double x0 = a, x1 = b;
        double xNext = x1;
        for (int i = 0; i < 1000; i++) {
            double f0 = f2(x0);
            double f1 = f2(x1);
            if (Math.abs(f1 - f0) < 1e-12) break;
            xNext = x1 - f1 * (x1 - x0) / (f1 - f0);
            if (Math.abs(xNext - x1) < eps) return xNext;
            x0 = x1;
            x1 = xNext;
        }
        return xNext;
    }
    public static void runTask2(double a, double b) {
        double eps = 1e-5;
        System.out.println("\n--- Результати Завдання 2 ---");

        // Перевірка теореми про існування кореня
        if (f2(a) * f2(b) > 0) {
            System.out.println("[Попередження] На кінцях інтервалу функція має однакові знаки.");
            System.out.println("Методи можуть не зійтися або на інтервалі парна кількість коренів (чи нуль).");
        }

        double rootDich = dichotomyMethod(a, b, eps);
        double rootNewt = newtonMethod((a + b) / 2, eps);
        double rootSec = secantMethod(a, b, eps);

        System.out.printf("Метод дихотомії: %.5f (f(x) = %.6f)\n", rootDich, f2(rootDich));
        System.out.printf("Метод дотичних:  %.5f (f(x) = %.6f)\n", rootNewt, f2(rootNewt));
        System.out.printf("Метод хорд:      %.5f (f(x) = %.6f)\n", rootSec, f2(rootSec));
    }
    public static double f3(double x, double y) {
        return Math.sin(x) - y;
    }

    public static void runTask3(double x0, double y0, double xEnd, double h) {
        System.out.println("\n--- Результати Завдання 3 (Рунге-Кутта 3-го порядку) ---");
        System.out.println("-------------------------");
        System.out.printf("|   %-7s |   %-7s |\n", "x", "y");
        System.out.println("-------------------------");

        double x = x0;
        double y = y0;

        System.out.printf("| %-9.4f | %-9.4f |\n", x, y);

        while (x < xEnd - 1e-9) {
            double k1 = h * f3(x, y);
            double k2 = h * f3(x + h / 2.0, y + k1 / 2.0);
            double k3 = h * f3(x + h, y - k1 + 2 * k2);

            y = y + (k1 + 4 * k2 + k3) / 6.0;
            x = x + h;

            System.out.printf("| %-9.4f | %-9.4f |\n", x, y);
        }
        System.out.println("-------------------------");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ВИКОНАННЯ ЛАБОРАТОРНОЇ РОБОТИ 2.1 ===");

        System.out.println("\n[Введення для Завдання 1 (Інтеграл)]");
        System.out.print("Введіть нижню межу інтегрування a (рекомендовано 1): ");
        double a1 = scanner.nextDouble();
        System.out.print("Введіть верхню межу інтегрування b (рекомендовано 2): ");
        double b1 = scanner.nextDouble();
        System.out.print("Введіть крок інтегрування h (рекомендовано 0,2): ");
        double h1 = scanner.nextDouble();
        runTask1(a1, b1, h1);

        System.out.println("\n[Введення для Завдання 2 (Корені рівняння)]");
        System.out.print("Введіть ліву межу інтервалу a (наприклад, 1,0): ");
        double a2 = scanner.nextDouble();
        System.out.print("Введіть праву межу інтервалу b (наприклад, 2,0): ");
        double b2 = scanner.nextDouble();
        runTask2(a2, b2);

        System.out.println("\n[Введення для Завдання 3 (Диференціальне рівняння)]");
        System.out.print("Введіть початкове x0 (наприклад, 0): ");
        double x0 = scanner.nextDouble();
        System.out.print("Введіть початкове y0 (наприклад, 1): ");
        double y0 = scanner.nextDouble();
        System.out.print("Введіть кінцеве x (наприклад, 1,0): ");
        double xEnd = scanner.nextDouble();
        System.out.print("Введіть крок h (наприклад, 0,1): ");
        double h3 = scanner.nextDouble();
        runTask3(x0, y0, xEnd, h3);

        scanner.close();
    }
}