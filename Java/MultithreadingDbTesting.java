import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class MultithreadingDbTesting {

    public static void main(String[] args) throws Exception {
        log("Starting to fetch Employees and Departments...");
        long startTime = System.currentTimeMillis();

        // Start async tasks
        CompletableFuture<List<Employee>> employeeTask = getEmployeesAsync();
        CompletableFuture<List<Department>> departmentTask = getDepartmentsAsync();

        // Wait for both to complete
        CompletableFuture.allOf(employeeTask, departmentTask).join();

        // Get results
        List<Employee> employees = employeeTask.get();
        List<Department> departments = departmentTask.get();

        long endTime = System.currentTimeMillis();
        log("Fetched Employees: " + employees.size());
        log("Fetched Departments: " + departments.size());
        log("Total Time: " + (endTime - startTime) + " ms");
    }

    public static CompletableFuture<List<Employee>> getEmployeesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log("Delay and Returned Completed Async in getEmployeesAsync");
            return Arrays.asList(
                new Employee(1, "Alice", 1),
                new Employee(2, "Bob", 2),
                new Employee(3, "Charlie", 1)
            );
        });
    }

    public static CompletableFuture<List<Department>> getDepartmentsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log("Delay and Returned Completed Async in getDepartmentsAsync");
            return Arrays.asList(
                new Department(1, "IT"),
                new Department(2, "HR"),
                new Department(3, "Finance")
            );
        });
    }

    public static void log(String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.println("[" + time + "] " + message);
    }
}

class Employee {
    public int id;
    public String name;
    public int departmentId;

    public Employee(int id, String name, int deptId) {
        this.id = id;
        this.name = name;
        this.departmentId = deptId;
    }
}

class Department {
    public int id;
    public String name;

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

/*
[17:48:07.427] Starting to fetch Employees and Departments...
[17:48:09.458] Delay and Returned Completed Async in getDepartmentsAsync
[17:48:12.458] Delay and Returned Completed Async in getEmployeesAsync
[17:48:12.459] Fetched Employees: 3
[17:48:12.459] Fetched Departments: 3
[17:48:12.462] Total Time: 5003 ms
*/
