package sogeti.carleaseleaseapi.controller;

import com.sogeti.carleaseinterestcontractapi.openapi.api.V1Api;
import com.sogeti.carleaseinterestcontractapi.openapi.model.InterestAddRequest;
import com.sogeti.carleaseinterestcontractapi.openapi.model.InterestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import sogeti.carleaseleaseapi.mapper.InterestMapper;
import sogeti.carleaseleaseapi.model.Interest;
import sogeti.carleaseleaseapi.service.InterestService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InterestController implements V1Api {

    private final InterestService interestService;
    private final InterestMapper interestMapper;

    @Override
    public ResponseEntity<InterestResponse> createInterestRateV1(@Valid InterestAddRequest interestAddRequest) {
        Interest interest = interestMapper.mapInterestAddRequestToInterest(interestAddRequest);
        Interest addedInterest = interestService.add(interest);
        InterestResponse interestResponse = interestMapper.mapInterestToInterestResponse(addedInterest);
        return ResponseEntity.ok(interestResponse);
    }

    @Override
    public ResponseEntity<Void> deleteInterestRateByIdV1(Long id) {
       interestService.delete(id);
       return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<InterestResponse> getInterestRateByDateV1(LocalDate date) {
        Interest interest = interestService.retrieve(date);
        InterestResponse interestResponse = interestMapper.mapInterestToInterestResponse(interest);
        return ResponseEntity.ok(interestResponse);
    }

    @Override
    public ResponseEntity<List<InterestResponse>> getInterestRatesV1() {
        List<Interest> interests = interestService.retrieveAll();
        List<InterestResponse> interestResponses = interests.stream()
                .map(interestMapper::mapInterestToInterestResponse)
                .toList();
        return ResponseEntity.ok(interestResponses);
    }
}
