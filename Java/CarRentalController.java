import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
public class CarRentalController {

    private List<Car> cars = new ArrayList<>();
    private List<RentalRecord> rentalRecords = new ArrayList<>();

    // Add a car
    @PostMapping("/add")
    public boolean addCar(@RequestBody Car car) {
        return cars.add(car);
    }

    // Get car by ID
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable int id) {
        return cars.stream()
                .filter(car -> car.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Get available cars
    @GetMapping("/available")
    public List<Car> getAvailableCars() {
        return cars.stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }

    // Rent a car
    @PostMapping("/rent")
    public void rentCar(@RequestParam int rentalId,
                        @RequestParam int carId,
                        @RequestParam int customerId,
                        @RequestParam String startDate) {
        Car car = getCarById(carId);
        if (car != null && car.isAvailable()) {
            car.setAvailable(false);
            RentalRecord record = new RentalRecord(rentalId, carId, customerId, LocalDate.parse(startDate), null, 0.0);
            rentalRecords.add(record);
        }
    }

    // Return a car
    @PostMapping("/return")
    public void returnCar(@RequestParam int rentalId,
                          @RequestParam String endDate) {
        RentalRecord record = rentalRecords.stream()
                .filter(r -> r.getRentalId() == rentalId && r.getEndDate() == null)
                .findFirst()
                .orElse(null);

        if (record != null) {
            LocalDate returnDate = LocalDate.parse(endDate);
            record.setEndDate(returnDate);
            long days = ChronoUnit.DAYS.between(record.getStartDate(), returnDate);
            record.setRentalFee(days * 50); // Assume $50 per day

            Car car = cars.stream()
                    .filter(c -> c.getId() == record.getCarId())
                    .findFirst()
                    .orElse(null);
            if (car != null) car.setAvailable(true);
        }
    }

    // Get all rental records
    @GetMapping("/rentals")
    public List<RentalRecord> getAllRentalRecords() {
        return rentalRecords;
    }

    // Get rentals for a car
    @GetMapping("/{carId}/rentals")
    public List<RentalRecord> getRentalsForCar(@PathVariable int carId) {
        return rentalRecords.stream()
                .filter(r -> r.getCarId() == carId)
                .collect(Collectors.toList());
    }

    // Get rentals for a customer
    @GetMapping("/customer/{customerId}/rentals")
    public List<RentalRecord> getRentalsForCustomer(@PathVariable int customerId) {
        return rentalRecords.stream()
                .filter(r -> r.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    // Most popular model
    @GetMapping("/popular")
    public String getMostPopularModel() {
        return rentalRecords.stream()
                .map(r -> getCarById(r.getCarId()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Car::getModel, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Total fees
    @GetMapping("/fees")
    public double getTotalRentalFees() {
        return rentalRecords.stream()
                .mapToDouble(RentalRecord::getRentalFee)
                .sum();
    }

    // Available by model & year
    @GetMapping("/available/{model}/{year}")
    public List<Car> getAvailableByModelYear(@PathVariable String model, @PathVariable int year) {
        return cars.stream()
                .filter(car -> car.isAvailable() &&
                        car.getModel().equalsIgnoreCase(model) &&
                        car.getYear() == year)
                .collect(Collectors.toList());
    }

    // Duration by car
    @GetMapping("/{carId}/duration")
    public long getRentalDurationForCar(@PathVariable int carId) {
        return rentalRecords.stream()
                .filter(r -> r.getCarId() == carId && r.getEndDate() != null)
                .mapToLong(r -> ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()))
                .sum();
    }

    // Fees by customer
    @GetMapping("/customer/{customerId}/fees")
    public double getTotalFeesForCustomer(@PathVariable int customerId) {
        return rentalRecords.stream()
                .filter(r -> r.getCustomerId() == customerId)
                .mapToDouble(RentalRecord::getRentalFee)
                .sum();
    }

    // Rental by ID
    @GetMapping("/rental/{rentalId}")
    public RentalRecord getRentalById(@PathVariable int rentalId) {
        return rentalRecords.stream()
                .filter(r -> r.getRentalId() == rentalId)
                .findFirst()
                .orElse(null);
    }
}
// ────────────────────── SERVICE CLASSES ──────────────────────

interface CarRentalService {
    Car addCar(Car car);
    Car getCarById(int id);
    List<Car> getAvailableCars();
    void rentCar(int rentalId, int carId, int customerId, String startDate);
    void returnCar(int rentalId, String endDate);
}

@Service
class CarRentalServiceImpl implements CarRentalService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalRecordRepository rentalRepository;
}


// ────────────────────── JPA REPOSITORY ──────────────────────
// JpaSpecificationExecutor<T>	— (separate interface)
// JpaRepository extends PagingAndSortingRepository extends CrudRepository extends Repository

interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByAvailableTrue();
}

interface RentalRecordRepository extends JpaRepository<RentalRecord, Integer> {}

// ────────────────────── MODEL CLASSES ──────────────────────

public static class Car {
    private int id;
    private String model;
    private int year;
    private boolean available;

    // Constructors
    public Car() {}
    public Car(int id, String model, int year, boolean available) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.available = available;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

public static class RentalRecord {
    private int rentalId;
    private int carId;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double rentalFee;

    // Constructors
    public RentalRecord() {}
    public RentalRecord(int rentalId, int carId, int customerId,
                        LocalDate startDate, LocalDate endDate, double rentalFee) {
        this.rentalId = rentalId;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalFee = rentalFee;
    }

    // Getters & Setters
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public double getRentalFee() { return rentalFee; }
    public void setRentalFee(double rentalFee) { this.rentalFee = rentalFee; }
}
