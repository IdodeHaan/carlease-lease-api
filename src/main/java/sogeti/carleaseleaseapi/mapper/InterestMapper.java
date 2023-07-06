package sogeti.carleaseleaseapi.mapper;

import com.sogeti.carleaseinterestcontractapi.openapi.model.InterestAddRequest;
import com.sogeti.carleaseinterestcontractapi.openapi.model.InterestResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import sogeti.carleaseleaseapi.model.Interest;

@Component
@RequiredArgsConstructor
public class InterestMapper {

    private final ModelMapper mapper;

    public Interest mapInterestAddRequestToInterest(InterestAddRequest interestAddRequest) {
        return mapper.map(interestAddRequest, Interest.class);
    }

    public InterestResponse mapInterestToInterestResponse(Interest interest) {
        return mapper.map(interest, InterestResponse.class);
    }
}
