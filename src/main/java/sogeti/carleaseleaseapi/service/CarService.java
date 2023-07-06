package sogeti.carleaseleaseapi.service;

import com.sogeti.carleasecarcontractapi.openapi.model.CalculationRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sogeti.carleaseleaseapi.exceptionhandling.ResourceNotFoundException;
import sogeti.carleaseleaseapi.model.Car;
import sogeti.carleaseleaseapi.repository.CarRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final InterestService interestService;


    public Car add(Car car) {
        car.setId(0L);
        return carRepository.save(car);
    }

    public Car retrieve(long id) {
        Optional<Car> _car = carRepository.findById(id);
        _car.orElseThrow(() -> new ResourceNotFoundException("Car not found - id " + id));
        return _car.get();
    }

    public List<Car> retrieveAll() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException("No cars found");
        }
        return cars;
    }

    public Car update(Long carId, Car car) {
        Car existingCar = retrieve(carId);
        return carRepository.save(Car.builder()
                .id(existingCar.getId())
                .make(existingCar.getMake())
                .model(existingCar.getModel())
                .version(existingCar.getVersion())
                .numberOfDoors(car.getNumberOfDoors())
                .co2Emission(car.getCo2Emission())
                .grossPrice(car.getGrossPrice())
                .nettPrice(car.getNettPrice())
                .build());
    }

    public void delete(long id) {
        retrieve(id);
        carRepository.deleteById(id);
    }

    public BigDecimal calculateLeaseRate(long carId, CalculationRequestBody calculationRequestBody) {
        Car car = retrieve(carId);
        LocalDate startDate = calculationRequestBody.getStartDate();
        Integer mileage = calculationRequestBody.getMileage();
        Integer duration = calculationRequestBody.getDuration();
        BigDecimal interest = interestService.retrieve(startDate).getRate();
        BigDecimal nettPrice = car.getNettPrice();

        BigDecimal totalMilageDividedByNettPrice = new BigDecimal((mileage / 12) * duration).divide(nettPrice, 2, RoundingMode.HALF_UP);
        BigDecimal interestOnNettPricePerMonth = interest.divide(new BigDecimal(100)).multiply(nettPrice).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        return totalMilageDividedByNettPrice.add(interestOnNettPricePerMonth);
    }
}
