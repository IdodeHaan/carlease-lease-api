package sogeti.carleaseleaseapi.service;

import com.sogeti.carleasecarcontractapi.openapi.model.CalculationRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sogeti.carleaseleaseapi.exceptionhandling.ResourceNotFoundException;
import sogeti.carleaseleaseapi.model.Proposal;
import sogeti.carleaseleaseapi.proxy.CarLeaseCustomerProxy;
import sogeti.carleaseleaseapi.repository.ProposalRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final CarService carService;
    private final CarLeaseCustomerProxy proxy;

    public Proposal add(Proposal proposal, String authToken) throws Exception {
        isCustomerExisting(proposal.getCustomerId(), authToken);
        BigDecimal leaseRate = getLeaseRate(proposal);
        proposal.setLeaseRate(leaseRate);
        proposal.setId(0L);
        return proposalRepository.save(proposal);
    }

    public void isCustomerExisting(long customerId, String authToken) throws Exception {
        try {
            proxy.getCustomerByIdV1(customerId, authToken);
        } catch (Exception exception) {
            if (exception.getMessage().contains("404")) {
                throw new ResourceNotFoundException("Customer not found - id " + customerId);
            } else {
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            }
        }
    }

    private BigDecimal getLeaseRate(Proposal proposal) {
        CalculationRequestBody calculationRequestBody = new CalculationRequestBody();
        calculationRequestBody.setStartDate(proposal.getStartDate());
        calculationRequestBody.setDuration(proposal.getDuration());
        calculationRequestBody.setMileage(proposal.getMileage());
        BigDecimal leaseRate = carService.calculateLeaseRate(proposal.getCar().getId(), calculationRequestBody);
        return leaseRate;
    }

    public Proposal retrieve(Long proposalId) {
        Optional<Proposal> _proposal = proposalRepository.findById(proposalId);
        return  _proposal.orElseThrow(() -> new ResourceNotFoundException("Proposal not found - id " + proposalId));
    }

    public void delete(Long proposalId) {
        retrieve(proposalId);
        proposalRepository.deleteById(proposalId);
    }

    public List<Proposal> retrieveAll() {
        List<Proposal> proposals = proposalRepository.findAll();
        if (proposals.isEmpty()) {
            throw new ResourceNotFoundException("No proposals found");
        }
        return proposals;
    }

    public Proposal update(Long proposalId, Proposal proposal) {
        Proposal existingProposal = retrieve(proposalId);
        proposal.setCar(existingProposal.getCar());
        BigDecimal leaseRate = getLeaseRate(proposal);
        return proposalRepository.save(Proposal.builder()
                .id(existingProposal.getId())
                .customerId(existingProposal.getCustomerId())
                .car(existingProposal.getCar())
                .mileage(proposal.getMileage())
                .duration(proposal.getDuration())
                .startDate(proposal.getStartDate())
                .leaseRate(leaseRate)
                .effectuated(proposal.isEffectuated())
                .build());
    }
}

