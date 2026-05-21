import java.util.Arrays;

class Student {
    String lastName;
    String firstName;
    String subject;
    int labsCompleted;

    public Student(String lastName, String firstName, String subject, int labsCompleted) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.subject = subject;
        this.labsCompleted = labsCompleted;
    }

    @Override
    public String toString() {
        return String.format("%-3d | %-12s | %-10s | %-15s",
                labsCompleted, lastName, firstName, subject);
    }
}
class SortingAlgorithms {

    public static void countingSort(Student[] arr) {
        if (arr.length == 0) return;

        int max = arr[0].labsCompleted;
        for (Student s : arr) if (s.labsCompleted > max) max = s.labsCompleted;

        int[] count = new int[max + 1];
        Student[] output = new Student[arr.length];

        for (Student s : arr) count[s.labsCompleted]++;

        for (int i = max - 1; i >= 0; i--) {
            count[i] += count[i + 1];
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i].labsCompleted] - 1] = arr[i];
            count[arr[i].labsCompleted]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }
    public static void radixSort(Student[] arr) {
        if (arr.length == 0) return;
        int max = arr[0].labsCompleted;
        for (Student s : arr) if (s.labsCompleted > max) max = s.labsCompleted;

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp);
        }
    }
    private static void countingSortByDigit(Student[] arr, int exp) {
        Student[] output = new Student[arr.length];
        int[] count = new int[10];

        for (Student s : arr) count[(s.labsCompleted / exp) % 10]++;

        for (int i = 8; i >= 0; i--) count[i] += count[i + 1];

        for (int i = arr.length - 1; i >= 0; i--) {
            int digit = (arr[i].labsCompleted / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    public static void mergeSortBottomUp(Student[] arr) {
        int n = arr.length;
        Student[] aux = new Student[n];
        for (int len = 1; len < n; len *= 2) {
            for (int lo = 0; lo < n - len; lo += len + len) {
                merge(arr, aux, lo, lo + len - 1, Math.min(lo + len + len - 1, n - 1));
            }
        }
    }

    private static void merge(Student[] a, Student[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) aux[k] = a[k];

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];

            else if (aux[j].labsCompleted > aux[i].labsCompleted) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }
}


public class Main {
    public static void printArray(Student[] arr, String title) {
        System.out.println("\n--- " + title + " ---");
        System.out.println("Лаб | Прізвище     | Ім'я       | Дисципліна");
        System.out.println("----------------------------------------------");
        for (Student s : arr) System.out.println(s);
    }

    public static void main(String[] args) {
        Student[] students = {
                new Student("Мельник", "Олег", "Програмування", 5),
                new Student("Шевченко", "Анна", "Фізика", 12),
                new Student("Бойко", "Іван", "Математика", 8),
                new Student("Ткаченко", "Марія", "Програмування", 12),
                new Student("Кравченко", "Петро", "Англійська", 3)
        };

        Student[] arr1 = students.clone();
        printArray(arr1, "Перед сортуванням (Підрахунком)");
        SortingAlgorithms.countingSort(arr1);
        printArray(arr1, "Після сортування (Підрахунком - Спадання)");

        Student[] arr2 = students.clone();
        SortingAlgorithms.radixSort(arr2);
        printArray(arr2, "Після сортування (Порозрядне - Спадання)");

        Student[] arr3 = students.clone();
        SortingAlgorithms.mergeSortBottomUp(arr3);
        printArray(arr3, "Після сортування (Висхідне злиття - Спадання)");
    }
}