import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class MultithreadingEmployeeBatchSaveSimulation {

    // Number of employees per batch when saving
    private static final int BATCH_SIZE = 1000;

    // Number of threads to use (twice the available processors for balanced concurrency)
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 20;

    public static void main(String[] args) {
        log("Starting to fetch Employees...");

        long startTime = System.currentTimeMillis();

        // Generate 1 million mock employees
        List<Employee> employees = generateEmployees(1_000_000);

        // Create a fixed thread pool executor for running batch save tasks
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Create batch save tasks for employees, splitting employees into batches
        List<Callable<Integer>> tasks = createBatchSaveTasks(employees, BATCH_SIZE);

        // Execute all batch save tasks and sum total saved employees
        int totalSaved = executeTasksAndSumResults(executor, tasks);

        // Shutdown executor gracefully
        executor.shutdown();

        long endTime = System.currentTimeMillis();

        log("Saved Employees: " + totalSaved);
        log("Total Time: " + (endTime - startTime) + " ms");
    }

    private static List<Employee> generateEmployees(int count) {
        List<Employee> employees = new ArrayList<>(count);
        Random rand = new Random();
        for (int i = 1; i <= count; i++) {
            employees.add(new Employee(i, "Employee_" + i, rand.nextInt(100) + 1));
        }
        return employees;
    }

    private static List<Callable<Integer>> createBatchSaveTasks(List<Employee> employees, int batchSize) {
        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < employees.size(); i += batchSize) {
            final int from = i;
            final int to = Math.min(i + batchSize, employees.size());
            final List<Employee> batch = employees.subList(from, to);
            final int batchIndex = i / batchSize;

            // Create task as a separate variable before adding
            Callable<Integer> task = new Callable<Integer>() {
                public Integer call() {
                    try {
                        // Simulate delay for saving batch to DB
                        Thread.sleep(100);

                        // Simulate error in 10th batch (index 9)
                        if (batchIndex == 9) {
                            throw new RuntimeException("Simulated error in batch " + batchIndex);
                        }

                        // log("Saved batch: " + batch.size());
                        return batch.size();
                    } catch (Exception e) {
                        log("Error saving batch: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            };

            tasks.add(task);
        }

        return tasks;
    }

    private static int executeTasksAndSumResults(ExecutorService executor, List<Callable<Integer>> tasks) {
        int totalSaved = 0;
        try {
            List<Future<Integer>> futures = executor.invokeAll(tasks);

            for (Future<Integer> future : futures) {
                try {
                    totalSaved += future.get();
                } catch (ExecutionException e) {
                    log("Batch failed: " + e.getCause());
                } catch (InterruptedException e) {
                    log("Batch interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        } catch (InterruptedException e) {
            log("invokeAll interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return totalSaved;
    }

    private static void log(String message) {
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
