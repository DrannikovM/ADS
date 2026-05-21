import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class Student {
    String lastName;
    String firstName;
    int course;
    int studentId;
    String gender;
    String residence;

    public Student(String lastName, String firstName, int course, int stidentId, String gender, String residence) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.course = course;
        this.studentId = stidentId;
        this.gender = gender;
        this.residence = residence;
    }
    @Override
    public String toString() {
        return String.format("| %-12s | %-10s | %-4d | %-8d | %-8s | %-15s |",
                lastName, firstName, course, studentId, gender, residence);
    }
}
class Node {
    Student data;
    Node left, right;

    public Node(Student student) {
        this.data = student;
        this.left = null;
        this.right = null;
    }
}
class BinaryTree {
    Node root;

    public void insert(Student student) {
        root = insertRecursive(root, student);
    }
    public Node insertRecursive(Node root, Student student) {
        if (root == null) {
            return new Node(student);
        }
        if (student.studentId < root.data.studentId) {
            root.left = insertRecursive(root.left, student);
        } else if (student.studentId > root.data.studentId) {
            root.right = insertRecursive(root.right, student);
        }
        return root;
    }
    public void printInOrder() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("| Прізвище     | Ім'я       | Курс | Квиток   | Стать    | Місце прож.     |");
        System.out.println("--------------------------------------------------------------------------------------");
        inOrderRecursive(root);
        System.out.println("--------------------------------------------------------------------------------------");
    }
    public void inOrderRecursive(Node root) {
        if (root != null) {
            inOrderRecursive(root.left);
            System.out.println(root.data);
            inOrderRecursive(root.right);
        }
    }
    public void printParallelInOrder() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("| Прізвище     | Ім'я       | Курс | Квиток   | Стать    | Місце прож.     |");
        System.out.println("--------------------------------------------------------------------------------------");

        if (root != null) {
            ForkJoinPool pool = new ForkJoinPool();
            List<Student> sortedStudents = pool.invoke(new ParallelInOrderTask(root));
            sortedStudents.forEach(System.out::println);
        }

        System.out.println("--------------------------------------------------------------------------------------");
    }
    private static class ParallelInOrderTask extends RecursiveTask<List<Student>> {
        private final Node node;

        public ParallelInOrderTask(Node node) {
            this.node = node;
        }

        @Override
        protected List<Student> compute() {
            List<Student> result = new ArrayList<>();
            if (node == null) return result;

            ParallelInOrderTask leftTask = new ParallelInOrderTask(node.left);
            leftTask.fork();

            ParallelInOrderTask rightTask = new ParallelInOrderTask(node.right);
            List<Student> rightResult = rightTask.compute();

            List<Student> leftResult = leftTask.join();

            result.addAll(leftResult);
            result.add(node.data);
            result.addAll(rightResult);

            return result;
        }
    }
    public List<Student> searchByCriteriaParallel() {
        if (root == null) return new ArrayList<>();
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new ParallelSearchTask(root));
    }
    private static class ParallelSearchTask extends RecursiveTask<List<Student>> {
        private final Node node;

        public ParallelSearchTask(Node node) {
            this.node = node;
        }

        @Override
        protected List<Student> compute() {
            List<Student> result = new ArrayList<>();
            if (node == null) return result;

            ParallelSearchTask leftTask = new ParallelSearchTask(node.left);
            ParallelSearchTask rightTask = new ParallelSearchTask(node.right);

            leftTask.fork();
            List<Student> rightResult = rightTask.compute();
            List<Student> leftResult = leftTask.join();

            Student s = node.data;
            if (s.gender.equalsIgnoreCase("Жіноча") && s.course == 1 && s.residence.equalsIgnoreCase("Гуртожиток")) {
                result.add(s);
            }

            result.addAll(leftResult);
            result.addAll(rightResult);
            return result;
        }
    }
    public List<Student> searchByCriteria() {
        List<Student> result = new ArrayList<>();
        searchRecursive(root, result);
        return result;
    }
    private void searchRecursive(Node root, List<Student> result) {
        if (root == null) return;

        Student s = root.data;
        if (s.gender.equalsIgnoreCase("Жіноча") &&
                s.course == 1 &&
                s.residence.equalsIgnoreCase("Гуртожиток")) {
            result.add(s);
        }
        searchRecursive(root.left, result);
        searchRecursive(root.right, result);
    }
    public void deleteByCriteria() {
        root = deleteRecursive(root);
    }
    private Node deleteRecursive(Node root) {
        if (root == null) return null;

        root.left = deleteRecursive(root.left);
        root.right = deleteRecursive(root.right);

        Student s = root.data;
        if (s.gender.equalsIgnoreCase("Жіноча") &&
                s.course == 1 &&
                s.residence.equalsIgnoreCase("Гуртожиток")) {
            return deleteNode(root, s.studentId);
        }
        return root;
    }
    private Node deleteNode(Node root, int key) {
        if (root == null) return null;

        if (key < root.data.studentId) root.left = deleteNode(root.left, key);
        else if (key > root.data.studentId) root.right = deleteNode(root.right, key);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            root.data = minValue(root.right);
            root.right = deleteNode(root.right, root.data.studentId);
        }
        return root;
    }
    private Student minValue(Node root) {
        Student minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }
}
public class Main {
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();

        tree.insert(new Student("Іванова", "Анна", 1, 105, "Жіноча", "Гуртожиток"));
        tree.insert(new Student("Петрова", "Іван", 2, 102, "Чоловіча", "Дім"));
        tree.insert(new Student("Сидорова", "Ольга", 1, 110, "Жіноча", "Гуртожиток"));
        tree.insert(new Student("Коваль", "Марія", 3, 101, "Жіноча", "Гуртожиток"));
        tree.insert(new Student("Бондар", "Олена", 1, 108, "Жіноча", "Квартира"));

        System.out.println("Вихідне дерево (Паралельний обхід):");
        tree.printParallelInOrder();

        System.out.println("\nРезультат пошуку (Студентки 1-го курсу в гуртожитку):");
        List<Student> found = tree.searchByCriteriaParallel();
        if (found.isEmpty()) {
            System.out.println("Нікого не знайдено.");
        } else {
            found.forEach(System.out::println);
        }

        System.out.println("\nВидалення знайдених вузлів...");
        tree.deleteByCriteria();

        System.out.println("\nДерево після видалення:");
        tree.printInOrder();
    }
}