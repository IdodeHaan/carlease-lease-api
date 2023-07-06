package sogeti.carleaseleaseapi.mapper;

import com.sogeti.carleasecarcontractapi.openapi.model.CarAddRequest;
import com.sogeti.carleasecarcontractapi.openapi.model.CarResponse;
import com.sogeti.carleasecarcontractapi.openapi.model.CarUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import sogeti.carleaseleaseapi.model.Car;

@Component
@RequiredArgsConstructor
public class CarMapper {

    private final ModelMapper mapper;

    public Car mapCarAddRequestToCar(CarAddRequest CarAddRequest) {
        return mapper.map(CarAddRequest, Car.class);
    }

    public Car mapCarUpdateRequestToCar(CarUpdateRequest carUpdateRequest) {
        return mapper.map(carUpdateRequest, Car.class);
    }

    public CarResponse mapCarToCarResponse(Car Car) {
        return mapper.map(Car, CarResponse.class);
    }
}
