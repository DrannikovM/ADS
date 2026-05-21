import java.util.*;

class Student implements Comparable<Student> {
    String lastName;
    String firstName;
    int course;
    String group;
    String city;

    public Student(String lastName, String firstName, int course, String group, String city) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.course = course;
        this.group = group;
        this.city = city;
    }

    @Override
    public int compareTo(Student other) {
        return this.lastName.compareTo(other.lastName);
    }

    @Override
    public String toString() {
        return String.format("| %-12s | %-10s | %-4d | %-6s | %-12s |",
                lastName, firstName, course, group, city);
    }
}
class Node {
    Student data;
    Node left, right;
    int size;

    public Node(Student student) {
        this.data = student;
        this.left = null;
        this.right = null;
        this.size = 1;
    }
}
class BinarySearchTree {
    Node root;
    private final Random rand = new Random();

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    private void updateSize(Node x) {
        if (x != null) {
            x.size = 1 + size(x.left) + size(x.right);
        }
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        updateSize(h);
        updateSize(x);
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        updateSize(h);
        updateSize(x);
        return x;
    }

    public void insertAtRoot(Student student) {
        root = insertAtRootRecursive(root, student);
    }

    private Node insertAtRootRecursive(Node h, Student student) {
        if (h == null) return new Node(student);

        int cmp = student.city.compareTo(h.data.city);
        if (cmp < 0) {
            h.left = insertAtRootRecursive(h.left, student);
            h = rotateRight(h);
        } else if (cmp > 0) {
            h.right = insertAtRootRecursive(h.right, student);
            h = rotateLeft(h);
        }
        updateSize(h);
        return h;
    }

    private Node insertAtLeaf(Node h, Student student) {
        if (h == null) return new Node(student);
        int cmp = student.city.compareTo(h.data.city);
        if (cmp < 0) h.left = insertAtLeaf(h.left, student);
        else if (cmp > 0) h.right = insertAtLeaf(h.right, student);
        updateSize(h);
        return h;
    }

    public void insertRandomized(Student student) {
        root = insertRandomizedRecursive(root, student);
    }

    private Node insertRandomizedRecursive(Node h, Student student) {
        if (h == null) return new Node(student);

        if (rand.nextInt(size(h) + 1) == 0) {
            return insertAtRootRecursive(h, student);
        }

        int cmp = student.city.compareTo(h.data.city);
        if (cmp < 0) h.left = insertRandomizedRecursive(h.left, student);
        else if (cmp > 0) h.right = insertRandomizedRecursive(h.right, student);

        updateSize(h);
        return h;
    }

    public Student search(String city) {
        Node result = searchRecursive(root, city);
        return result != null ? result.data : null;
    }

    private Node searchRecursive(Node x, String city) {
        if (x == null) return null;
        int cmp = city.compareTo(x.data.city);
        if (cmp < 0) return searchRecursive(x.left, city);
        else if (cmp > 0) return searchRecursive(x.right, city);
        else return x;
    }

    public void printLevelOrder() {
        if (root == null) {
            System.out.println("Дерево порожнє.");
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        System.out.println("-----------------------------------------------------------------");
        System.out.println("| Прізвище     | Ім'я       | Курс | Група  | Місто (КЛЮЧ) |");
        System.out.println("-----------------------------------------------------------------");

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.println(current.data);

            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        System.out.println("-----------------------------------------------------------------");
    }
}
public class Main {
    public static int binarySearch(Student[] arr, String lastName) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = arr[mid].lastName.compareTo(lastName);

            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return mid;
        }
        return -1;
    }
    public static void main(String[] args) {
        System.out.println("=== ЗАВДАННЯ 1 ===");

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Мельник", "Іван", 1, "КН-11", "Київ"));
        studentList.add(new Student("Шевченко", "Олена", 2, "КН-21", "Донецьк"));
        studentList.add(new Student("Коваленко", "Петро", 3, "КН-32", "Львів"));
        studentList.add(new Student("Бондаренко", "Марія", 1, "КН-12", "Харків"));
        studentList.add(new Student("Ткаченко", "Андрій", 4, "КН-41", "Донецьк"));
        studentList.add(new Student("Кравченко", "Анна", 2, "КН-22", "Одеса"));
        studentList.add(new Student("Олійник", "Сергій", 1, "КН-11", "Дніпро"));
        studentList.add(new Student("Поліщук", "Дмитро", 3, "КН-31", "Донецьк"));
        studentList.add(new Student("Лисенко", "Ольга", 2, "КН-21", "Полтава"));
        studentList.add(new Student("Руденко", "Олексій", 4, "КН-42", "Київ"));
        studentList.add(new Student("Вовк", "Тетяна", 1, "КН-12", "Запоріжжя"));
        studentList.add(new Student("Мороз", "Володимир", 3, "КН-32", "Миколаїв"));
        studentList.add(new Student("Козак", "Наталія", 2, "КН-22", "Донецьк"));
        studentList.add(new Student("Котенко", "Ігор", 1, "КН-11", "Чернігів"));
        studentList.add(new Student("Швець", "Юлія", 4, "КН-41", "Вінниця"));
        studentList.add(new Student("Коваль", "Максим", 3, "КН-31", "Суми"));
        studentList.add(new Student("Павленко", "Світлана", 2, "КН-21", "Житомир"));
        studentList.add(new Student("Харченко", "Артем", 1, "КН-12", "Донецьк"));
        studentList.add(new Student("Клименко", "Вікторія", 3, "КН-32", "Ужгород"));
        studentList.add(new Student("Яковенко", "Денис", 4, "КН-42", "Чернівці"));

        Student[] studentsArray = studentList.toArray(new Student[0]);

        Arrays.sort(studentsArray);

        System.out.println("Вміст відсортованого масиву студентів:");
        for (Student s : studentsArray) System.out.println(s);

        String targetLastName = "Шевченко";
        System.out.println("\nШукаємо студента за прізвищем: " + targetLastName);

        int foundIndex = binarySearch(studentsArray, targetLastName);

        if (foundIndex != -1) {
            Student foundStudent = studentsArray[foundIndex];
            System.out.println("Знайдено: " + foundStudent);

            if (foundStudent.city.equalsIgnoreCase("Донецьк")) {
                System.out.println("Умова виконана (місто Донецьк). Видаляємо студента...");

                Student[] newArray = new Student[studentsArray.length - 1];
                System.arraycopy(studentsArray, 0, newArray, 0, foundIndex);
                System.arraycopy(studentsArray, foundIndex + 1, newArray, foundIndex, studentsArray.length - foundIndex - 1);
                studentsArray = newArray;

                System.out.println("\nМасив після видалення студента:");
                for (Student s : studentsArray) System.out.println(s);
            } else {
                System.out.println("Студент знайдений, але він не з Донецька. Видалення скасовано.");
            }
        } else {
            System.out.println("Студента з таким прізвищем не знайдено.");
        }

        System.out.println("\n=== ЗАВДАННЯ 2 (Вставка в корінь BST, ключ: Місто) ===");
        BinarySearchTree bst = new BinarySearchTree();

        bst.insertAtRoot(new Student("Мельник", "Іван", 1, "КН-11", "Київ"));
        bst.insertAtRoot(new Student("Шевченко", "Олена", 2, "КН-21", "Донецьк"));
        bst.insertAtRoot(new Student("Коваленко", "Петро", 3, "КН-32", "Львів"));

        System.out.println("Обхід дерева в ширину після вставки 3-х елементів в корінь:");
        bst.printLevelOrder();

        String searchCity = "Донецьк";
        System.out.println("Пошук у дереві за містом '" + searchCity + "':");
        Student foundTreeStudent = bst.search(searchCity);
        System.out.println(foundTreeStudent != null ? foundTreeStudent : "Нічого не знайдено.");

        System.out.println("\n=== ЗАВДАННЯ 3 (Рандомізоване дерево - Балансування) ===");
        BinarySearchTree randomizedBst = new BinarySearchTree();

        randomizedBst.insertRandomized(new Student("Мельник", "Іван", 1, "КН-11", "Київ"));
        randomizedBst.insertRandomized(new Student("Шевченко", "Олена", 2, "КН-21", "Донецьк"));
        randomizedBst.insertRandomized(new Student("Коваленко", "Петро", 3, "КН-32", "Львів"));
        randomizedBst.insertRandomized(new Student("Бондаренко", "Марія", 1, "КН-12", "Харків"));

        System.out.println("Обхід рандомізованого BST дерева в ширину:");
        randomizedBst.printLevelOrder();

        String searchCity2 = "Львів";
        System.out.println("Пошук у рандомізованому дереві за містом '" + searchCity2 + "':");
        Student foundTreeStudent2 = randomizedBst.search(searchCity2);
        System.out.println(foundTreeStudent2 != null ? foundTreeStudent2 : "Нічого не знайдено.");
    }
}