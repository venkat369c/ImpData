import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        // Student collection
        List<Student> studentList = Arrays.asList(
            new Student(1, "Siva", 18, 1),
            new Student(2, "Ram", 21, 1),
            new Student(3, "Venky", 18, 2),
            new Student(4, "Babu", 20, 2),
            new Student(5, "Krishna", 21, null)
        );

        System.out.println("*** FirstOrDefault equivalent ***");
        studentList.stream()
            .filter(studentObj -> studentObj.getAge() > 18)
            .findFirst()
            .ifPresentOrElse(
                s -> System.out.println("First match: " + s.getStudentName()),
                () -> System.out.println("No student found")
            );

        System.out.println("*** LastOrDefault equivalent ***");
        studentList.stream()
            .filter(studentObj -> studentObj.getAge() > 18)
            .reduce((first, second) -> second)
            .ifPresentOrElse(
                s -> System.out.println("Last match: " + s.getStudentName()),
                () -> System.out.println("No student found")
            );

        System.out.println("*** Where with index (AtomicInteger) ***");
        AtomicInteger index = new AtomicInteger(0);
        List<Student> filteredResult = studentList.stream()
            .filter(studentObj -> index.getAndIncrement() % 2 == 0)
            .collect(Collectors.toList());
        filteredResult.forEach(s -> System.out.println(s.getStudentName()));

        System.out.println("*** ThenBy with OrderBy ***");
        studentList.stream()
            .sorted(Comparator.comparing(Student::getStudentName)
                    .thenComparing(Student::getAge))
            .forEach(s -> System.out.println(s.getStudentName()));

        System.out.println("*** GroupBy ***");
        studentList.stream()
            .collect(Collectors.groupingBy(Student::getAge))
            .forEach((age, studentGroup) -> {
                System.out.println("Age Group: " + age);
                studentGroup.forEach(s -> System.out.println("Student Name: " + s.getStudentName()));
            });

        List<Standard> standardList = Arrays.asList(
            new Standard(1, "Standard 1"),
            new Standard(2, "Standard 2"),
            new Standard(3, "Standard 3")
        );

        System.out.println("*** Join ***");
        studentList.stream()
            .flatMap(studentObj -> standardList.stream()
                .filter(std -> Objects.equals(std.getStandardID(), studentObj.getStandardID()))
                .map(std -> studentObj.getStudentName() + " - " + std.getStandardName()))
            .forEach(System.out::println);

        System.out.println("*** GroupJoin / Left Outer Join ***");
        standardList.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                std -> studentList.stream()
                    .filter(studentObj -> Objects.equals(studentObj.getStandardID(), std.getStandardID()))
                    .collect(Collectors.toList())
            ))
            .forEach((std, studentGroup) -> {
                System.out.println(std.getStandardName());
                studentGroup.forEach(s -> System.out.println(s.getStudentName()));
            });

        System.out.println("*** Select ***");
        studentList.stream()
            .map(studentObj -> Map.of("Name", studentObj.getStudentName(), "Age", studentObj.getAge()))
            .forEach(item ->
                System.out.println("Student Name: " + item.get("Name") + ", Age: " + item.get("Age")));

        System.out.println("*** All ***");
        boolean areAllStudentsTeenAger = studentList.stream()
            .allMatch(studentObj -> studentObj.getAge() > 12 && studentObj.getAge() < 20);
        System.out.println(areAllStudentsTeenAger);

        System.out.println("*** Any ***");
        boolean isAnyStudentTeenAger = studentList.stream()
            .anyMatch(studentObj -> studentObj.getAge() > 12 && studentObj.getAge() < 20);
        System.out.println(isAnyStudentTeenAger);

        System.out.println("*** Contains (custom comparator) ***");
        Student searchStudentObj = new Student(3, "Bill", 18, 2);
        boolean contains = studentList.stream()
            .anyMatch(studentObj -> new StudentComparer().equals(studentObj, searchStudentObj));
        System.out.println("Contains Bill? " + contains);

        System.out.println("Successfully completed !");
    }
}

class Student {
    private int studentID;
    private String studentName;
    private int age;
    private Integer standardID; // nullable

    public Student(int studentID, String studentName, int age, Integer standardID) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.age = age;
        this.standardID = standardID;
    }

    public int getStudentID() { return studentID; }
    public String getStudentName() { return studentName; }
    public int getAge() { return age; }
    public Integer getStandardID() { return standardID; }
}

class Standard {
    private int standardID;
    private String standardName;

    public Standard(int standardID, String standardName) {
        this.standardID = standardID;
        this.standardName = standardName;
    }

    public int getStandardID() { return standardID; }
    public String getStandardName() { return standardName; }
}

// Custom comparator like C#'s StudentComparer
class StudentComparer implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Integer.compare(s1.getStudentID(), s2.getStudentID());
    }

    public boolean equals(Student s1, Student s2) {
        return s1 != null && s2 != null && s1.getStudentID() == s2.getStudentID();
    }
}
