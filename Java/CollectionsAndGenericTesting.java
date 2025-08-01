import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CollectionsAndGenericTesting {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");

        // java.util.Scanner scanner = new java.util.Scanner(System.in);
        // System.out.print("Enter your name: ");
        // String name = scanner.nextLine();

        String[] stringArray = {"1", "2", "3", "4", "5"};

        // #region Non-Generic-Collections
        java.util.ArrayList<String> valsList = new java.util.ArrayList<>(java.util.Arrays.asList("Val1", "Val2"));

        // ArrayList (non-generic equivalent using Object)
        ArrayList<Object> al = new ArrayList<>();
        al.add("Testing");
        al.add(7);
        al.add(LocalDate.parse("8-Oct-1985", formatter));
        System.out.println("ArrayListData :: " + al.get(0));
        for (Object o : al) {
            System.out.println("ArrayListData :: " + o);
        }

        // Hashtable (Java's Hashtable is generic)
        Hashtable<String, Object> ht = new Hashtable<>();
        ht.put("ora", LocalDate.parse("8-Oct-1985", formatter));
        ht.put("vb", 333);
        ht.put("cs", "cs.net");
        ht.put("asp", "asp.net");
        System.out.println("HashtableData :: " + ht.get("cs"));
        for (Map.Entry<String, Object> entry : ht.entrySet()) {
            System.out.println("HashtableData :: " + entry.getKey() + " " + entry.getValue());
        }

        // SortedMap instead of SortedList
        SortedMap<String, Object> sl = new TreeMap<>();
        sl.put("ora", LocalDate.parse("8-Oct-1985", formatter));
        sl.put("vb", 333);
        sl.put("cs", "cs.net");
        sl.put("asp", "asp.net");
        System.out.println("SortedListData :: " + sl.get("asp"));
        for (Map.Entry<String, Object> entry : sl.entrySet()) {
            System.out.println("SortedListData :: " + entry.getKey() + " " + entry.getValue());
        }

        // Stack (non-generic)
        Stack<Object> stk = new Stack<>();
        stk.push("cs.net");
        stk.push(LocalDate.parse("8-Oct-1985", formatter));
        stk.push(333);
        stk.push("sqlserver");
        System.out.println("StackData :: " + stk.pop());
        for (Object o : stk) {
            System.out.println("StackData :: " + o);
        }

        // Queue (non-generic via LinkedList<Object>)
        Queue<Object> q = new LinkedList<>();
        q.add("cs.net");
        q.add(LocalDate.parse("8-Oct-1985", formatter));
        q.add(333);
        q.add("sqlserver");
        System.out.println("QueueData :: " + q.poll());
        for (Object o : q) {
            System.out.println("QueueData :: " + o);
        }

        // #endregion Non-Generic-Collections

        // #region Generic-Collections

        // List
        List<Employee> lst = new ArrayList<>();
        lst.add(new Employee(101, "Name1"));
        lst.add(new Employee(102, "Name2"));
        lst.add(new Employee(103, "Name3"));
        lst.add(new Employee(104, "Name4"));
        System.out.println("ListData :: " + lst.get(1).empId);
        for (Employee i : lst) {
            System.out.println("ListData :: " + i.empId);
        }

        // HashSet
        Set<String> set = new HashSet<>();
        set.add("Apple");
        set.add("Banana");
        set.add("Apple");
        List<String> setList = new ArrayList<>(set);
        if (setList.size() > 1)
            System.out.println("HashSetData :: " + setList.get(1));
        for (String fruit : set) {
            System.out.println("HashSetData :: " + fruit);
        }

        // Dictionary equivalent: HashMap
        Map<Integer, Employee> dct = new HashMap<>();
        dct.put(1, new Employee(101, "Name1"));
        dct.put(3, new Employee(103, "Name3"));
        dct.put(2, new Employee(102, "Name2"));
        dct.put(4, new Employee(104, "Name4"));
        System.out.println("DictionaryData :: " + dct.get(3).empId);
        for (Map.Entry<Integer, Employee> kvp : dct.entrySet()) {
            System.out.println("DictionaryData :: " + kvp.getKey() + " " + kvp.getValue().empId);
        }

        // SortedMap (TreeMap) for generic SortedList
        SortedMap<Integer, Employee> sortedList = new TreeMap<>();
        sortedList.put(1, new Employee(101, "Name1"));
        sortedList.put(3, new Employee(103, "Name3"));
        sortedList.put(2, new Employee(102, "Name2"));
        sortedList.put(4, new Employee(104, "Name4"));
        System.out.println("SortedListData :: " + sortedList.get(3).empId);
        for (Map.Entry<Integer, Employee> kvp : sortedList.entrySet()) {
            System.out.println("SortedListData :: " + kvp.getKey() + " " + kvp.getValue().empId);
        }

        // Stack
        Stack<Employee> stack = new Stack<>();
        stack.push(new Employee(101, "Name1"));
        stack.push(new Employee(103, "Name3"));
        stack.push(new Employee(102, "Name2"));
        stack.push(new Employee(104, "Name4"));
        System.out.println("StackData :: " + stack.pop().empId);
        for (Employee s : stack) {
            System.out.println("StackData :: " + s.empId);
        }

        // Queue
        Queue<Employee> queue = new LinkedList<>();
        queue.add(new Employee(101, "Name1"));
        queue.add(new Employee(103, "Name3"));
        queue.add(new Employee(102, "Name2"));
        queue.add(new Employee(104, "Name4"));
        System.out.println("QueueData :: " + queue.poll().empId);
        for (Employee s : queue) {
            System.out.println("QueueData :: " + s.empId);
        }

        // LinkedList
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.addLast(100);
        linkedList.addLast(20);
        linkedList.addFirst(5);
        System.out.println("LinkedListData :: " + linkedList.get(2));
        System.out.println("LinkedList after adding 5, 10, 20:");
        for (int number : linkedList) {
            System.out.println("LinkedListData :: " + number);
        }

        ListIterator<Integer> itr = linkedList.listIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(100)) {
                itr.add(101);
                break;
            }
        }

        System.out.println("LinkedList After inserting 101 after 100:");
        for (int number : linkedList) {
            System.out.println("LinkedListData :: " + number);
        }

        // #endregion Generic-Collections

        // #region Arrays
        int[] emptyArray;
        int[] defaultValues = new int[5];
        int[] primeNumbers = new int[] { 2, 3, 5 };
        int[] numbers = { 2, 3, 5, 7, 11 };

        primeNumbers[1] = 17;
        System.out.println(Arrays.toString(primeNumbers));
        System.out.println("Prime numbers length: " + primeNumbers.length);

        int[] unsortedNumbers = { 5, 3, 8, 1, 9 };
        Arrays.sort(unsortedNumbers);
        System.out.println("Sort :: " + Arrays.toString(unsortedNumbers));

        reverseArray(unsortedNumbers);
        System.out.println("Reverse :: " + Arrays.toString(unsortedNumbers));

        int[] copiedPrimes = Arrays.copyOf(primeNumbers, primeNumbers.length);
        System.out.println("Copy :: " + Arrays.toString(copiedPrimes));

        primeNumbers = Arrays.copyOf(primeNumbers, 7);
        primeNumbers[5] = 19;
        primeNumbers[6] = 23;
        System.out.println("Resize :: " + Arrays.toString(primeNumbers));

        int max = Arrays.stream(primeNumbers).max().orElse(0);
        int min = Arrays.stream(primeNumbers).min().orElse(0);
        System.out.println("Max and Min prime number: " + max + " and " + min);

        // 2D Array
        int[][] matrix2D = new int[][] { { 1, 2, 3 }, { 4, 5, 6 } };
        System.out.println("2D matrix:");
        for (int[] row : matrix2D) {
            for (int col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }

        // Jagged array
        int[][] jaggedArray = new int[3][];
        jaggedArray[0] = new int[] { 1, 2 };
        jaggedArray[1] = new int[] { 3, 4, 5 };
        jaggedArray[2] = new int[] { 6 };
        System.out.println("Jagged array:");
        for (int i = 0; i < jaggedArray.length; i++) {
            System.out.print("Row " + i + ": ");
            for (int value : jaggedArray[i]) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        // #endregion Arrays
    }

    public static void reverseArray(int[] arr) {
        int i = 0, j = arr.length - 1;
        while (i < j) {
            int temp = arr[i];
            arr[i++] = arr[j];
            arr[j--] = temp;
        }
    }
}

class GenericList<T> {
    public void add(T input) { }
}

class Employee {
    public int empId;
    public String empName;

    public Employee(int id, String name) {
        this.empId = id;
        this.empName = name;
    }

    public Employee() {}
}

/*
ArrayListData :: Testing
ArrayListData :: Testing
ArrayListData :: 7
ArrayListData :: 1985-10-08
HashtableData :: cs.net
HashtableData :: asp asp.net
HashtableData :: cs cs.net
HashtableData :: vb 333
HashtableData :: ora 1985-10-08
SortedListData :: asp.net
SortedListData :: asp asp.net
SortedListData :: cs cs.net
SortedListData :: ora 1985-10-08
SortedListData :: vb 333
StackData :: sqlserver
StackData :: cs.net
StackData :: 1985-10-08
StackData :: 333
QueueData :: cs.net
QueueData :: 1985-10-08
QueueData :: 333
QueueData :: sqlserver
ListData :: 102
ListData :: 101
ListData :: 102
ListData :: 103
ListData :: 104
HashSetData :: Banana
HashSetData :: Apple
HashSetData :: Banana
DictionaryData :: 103
DictionaryData :: 1 101
DictionaryData :: 2 102
DictionaryData :: 3 103
DictionaryData :: 4 104
SortedListData :: 103
SortedListData :: 1 101
SortedListData :: 2 102
SortedListData :: 3 103
SortedListData :: 4 104
StackData :: 104
StackData :: 101
StackData :: 103
StackData :: 102
QueueData :: 101
QueueData :: 103
QueueData :: 102
QueueData :: 104
LinkedListData :: 20
LinkedList after adding 5, 10, 20:
LinkedListData :: 5
LinkedListData :: 100
LinkedListData :: 20
LinkedList After inserting 101 after 100:
LinkedListData :: 5
LinkedListData :: 100
LinkedListData :: 101
LinkedListData :: 20
[2, 17, 5]
Prime numbers length: 3
Sort :: [1, 3, 5, 8, 9]
Reverse :: [9, 8, 5, 3, 1]
Copy :: [2, 17, 5]
Resize :: [2, 17, 5, 0, 0, 19, 23]
Max and Min prime number: 23 and 0
2D matrix:
1 2 3 
4 5 6 
Jagged array:
Row 0: 1 2 
Row 1: 3 4 5 
Row 2: 6 
*/
