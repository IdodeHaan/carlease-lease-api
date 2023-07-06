package sogeti.carleaseleaseapi.controller;

import com.sogeti.carleasecarcontractapi.openapi.api.V1Api;
import com.sogeti.carleasecarcontractapi.openapi.model.CalculationRequestBody;
import com.sogeti.carleasecarcontractapi.openapi.model.CarAddRequest;
import com.sogeti.carleasecarcontractapi.openapi.model.CarResponse;
import com.sogeti.carleasecarcontractapi.openapi.model.CarUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import sogeti.carleaseleaseapi.mapper.CarMapper;
import sogeti.carleaseleaseapi.model.Car;
import sogeti.carleaseleaseapi.service.CarService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CarController implements V1Api {

    private final CarMapper carMapper;
    private final CarService carService;

    @Override
    public ResponseEntity<BigDecimal> calculateLeaseRateV1(Long carId, @Valid CalculationRequestBody calculationRequestBody) {
        BigDecimal leaseRate = carService.calculateLeaseRate(carId, calculationRequestBody);
        return ResponseEntity.ok(leaseRate);
    }

    @Override
    public ResponseEntity<CarResponse> createCarV1(@Valid CarAddRequest carAddRequest) {
        Car car = carMapper.mapCarAddRequestToCar(carAddRequest);
        Car addedCar = carService.add(car);
        CarResponse carResponse = carMapper.mapCarToCarResponse(addedCar);
        return ResponseEntity.ok(carResponse);
    }

    @Override
    public ResponseEntity<Void> deleteCarByIdV1(Long carId) {
        carService.delete(carId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<CarResponse> getCarByIdV1(Long carId) {
        Car car = carService.retrieve(carId);
        CarResponse carResponse = carMapper.mapCarToCarResponse(car);
        return ResponseEntity.ok(carResponse);
    }

    @Override
    public ResponseEntity<List<CarResponse>> listCarsV1() {
        List<Car> cars = carService.retrieveAll();
        List<CarResponse> carResponses = cars.stream()
                .map(carMapper::mapCarToCarResponse)
                .toList();
        return ResponseEntity.ok(carResponses);
    }

    @Override
    public ResponseEntity<CarResponse> updateCarV1(Long carId, @Valid CarUpdateRequest carUpdateRequest) {
        Car car = carMapper.mapCarUpdateRequestToCar(carUpdateRequest);
        Car updatedCar = carService.update(carId, car);
        CarResponse carResponse = carMapper.mapCarToCarResponse(updatedCar);
        return ResponseEntity.ok(carResponse);
    }
}
