package sogeti.carleaseleaseapi.mapper;

import com.sogeti.carleaseproposalcontractapi.openapi.model.ProposalAddRequest;
import com.sogeti.carleaseproposalcontractapi.openapi.model.ProposalResponse;
import com.sogeti.carleaseproposalcontractapi.openapi.model.ProposalUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Component;
import sogeti.carleaseleaseapi.model.Car;
import sogeti.carleaseleaseapi.model.Proposal;
import sogeti.carleaseleaseapi.service.CarService;

@Component
@RequiredArgsConstructor
public class ProposalMapper {

    private final ModelMapper mapper;
    private final CarService carService;

    public Proposal mapProposalAddRequestToProposal(ProposalAddRequest proposalAddRequest) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Proposal proposal = mapper.map(proposalAddRequest, Proposal.class);
        Car car = carService.retrieve(proposalAddRequest.getCarId());
        proposal.setCar(car);
        return proposal;
    }

    public Proposal mapProposalUpdateRequestToProposal(ProposalUpdateRequest proposalUpdateRequest) {
        return mapper.map(proposalUpdateRequest, Proposal.class);
    }

    public ProposalResponse mapProposalToProposalResponse(Proposal proposal) {
        ProposalResponse proposalResponse = mapper.map(proposal, ProposalResponse.class);
        proposalResponse.setCarId(proposal.getCar().getId());
        return proposalResponse;
    }
}
