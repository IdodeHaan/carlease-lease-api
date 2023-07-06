package sogeti.carleaseleaseapi.service;

import com.sogeti.carleasecarcontractapi.openapi.model.CalculationRequestBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogeti.carleaseleaseapi.exceptionhandling.ResourceNotFoundException;
import sogeti.carleaseleaseapi.model.Car;
import sogeti.carleaseleaseapi.model.Interest;
import sogeti.carleaseleaseapi.repository.CarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CarServiceTest {

    @Autowired
    private CarService carService;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private InterestService interestService;

    private Car car;
    private Car createdCar;

    @BeforeEach
    void setup() {
        //given
        car = Car.builder()
                .make("Volkswagen")
                .model("Golf")
                .version("2.0 tdi")
                .numberOfDoors(5)
                .co2Emission(130)
                .grossPrice(new BigDecimal("37000.00"))
                .nettPrice(new BigDecimal("31400.00"))
                .build();
        //when
        createdCar = carService.add(car);
    }

    @AfterEach
    void cleanup() {
        carRepository.delete(createdCar);
    }

    @Test
    void testAdd_carIsCreated_whenCorrectDetailsAreProvided() {
        //then
        assertThat(createdCar.getId()).isGreaterThan(0);
    }

    @Test
    void testRetrieve_carIsRetrieved_whenExistingIdIsGiven() {
        //when
        Car retrievedCar = carService.retrieve(createdCar.getId());
        //then
        assertThat(retrievedCar.getId()).isEqualTo(createdCar.getId());
    }

    @Test
    void testRetrieve_throwsResourceNotFoundException_whenIdDoesNotExist() {
        //when & then
        assertThatThrownBy(() -> carService.retrieve(createdCar.getId() + 1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testRetrieveAll_oneMoreCareIsRetrieved_whenACarIsAdded() {
        //when
        int numberOfCars = carService.retrieveAll().size();
        Car extraCar = Car.builder()
                .make("someBrand")
                .model("someModel")
                .version("someVersion")
                .numberOfDoors(3)
                .co2Emission(100)
                .grossPrice(new BigDecimal("20000.00"))
                .nettPrice(new BigDecimal("18000.00"))
                .build();
        createdCar = carService.add(extraCar);
        int newNumberOfCars = carService.retrieveAll().size();
        //then
        assertThat(newNumberOfCars).isEqualTo(numberOfCars + 1);
    }

    @Test
    //Todo test can't be performed if there are cars in db that are also in a proposal
    void testRetrieveAll_throwsResourceNotFoundException_whenNoCarsAreFound() {
        //given
        carRepository.deleteAll();
        //when & then
        assertThatThrownBy(() -> carService.retrieveAll())
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUpdate_whenCarDetailsAreUpdated_AllPropertiesAreUpdated() {
        //given
        createdCar.setNumberOfDoors(9);
        createdCar.setCo2Emission(999);
        createdCar.setGrossPrice(new BigDecimal("99900.00"));
        createdCar.setNettPrice(new BigDecimal("55000.00"));
        //when
        Car updatedCar = carService.update(createdCar.getId(), createdCar);
        //then
        assertThat(updatedCar).usingRecursiveComparison().isEqualTo(createdCar);
    }

    @Test
    void testUpdate_throwsResourceNotFoundException_whenNonExistingCarIsUpdated() {
        //given
        createdCar.setId(createdCar.getId() + 1);
        //when & then
        assertThatThrownBy(() -> carService.update(createdCar.getId(), createdCar))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testDelete_afterDelete_returnedNumberOfCarsIsOneLess() {
        //given
        Car extraCar = Car.builder()
                .make("someBrand")
                .model("someModel")
                .version("someVersion")
                .numberOfDoors(3)
                .co2Emission(100)
                .grossPrice(new BigDecimal("20000.00"))
                .nettPrice(new BigDecimal("18000.00"))
                .build();
        createdCar = carService.add(extraCar);
        int numberOfCarsBefore = carService.retrieveAll().size();
        //when
        carService.delete(createdCar.getId());
        int numberOfCarsAfter = carService.retrieveAll().size();
        //then
        assertThat(numberOfCarsAfter).isEqualTo(numberOfCarsBefore - 1);
    }

    @Test
    void testDelete_throwsResourceNotFoundException_whenNonExistingCarIsDeleted() {
        //when & then
        assertThatThrownBy(() -> carService.delete(createdCar.getId() + 1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testCalculateLeaseRate_rateIsCalculated_whenCorrectDetailsAreGiven() {
        //given
        long carId = createdCar.getId();
        createdCar.setNettPrice(new BigDecimal("63000"));
        carService.update(carId, createdCar);
        CalculationRequestBody requestBody = new CalculationRequestBody();
        requestBody.setMileage(45000);
        requestBody.setDuration(60);
        LocalDate startDate = LocalDate.parse("2023-01-01");
        requestBody.setStartDate(startDate);
        try {
            Interest existingInterest = interestService.retrieve(startDate);
            interestService.delete(existingInterest.getId());
        } catch (Exception ignored) {

        }
        Interest interest = new Interest();
        interest.setRate(new BigDecimal("4.5"));
        interest.setStartDate(startDate);
        interestService.add(interest);
        BigDecimal expectedRate = new BigDecimal("239.82");
        //when
        BigDecimal calculatedRate = carService.calculateLeaseRate(carId, requestBody);
        //then
        assertThat(calculatedRate).isEqualTo(expectedRate);
    }
}