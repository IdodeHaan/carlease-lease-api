package sogeti.carleaseleaseapi.controller;

import com.sogeti.carleaseproposalcontractapi.openapi.api.V1Api;
import com.sogeti.carleaseproposalcontractapi.openapi.model.ProposalAddRequest;
import com.sogeti.carleaseproposalcontractapi.openapi.model.ProposalResponse;
import com.sogeti.carleaseproposalcontractapi.openapi.model.ProposalUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sogeti.carleaseleaseapi.mapper.ProposalMapper;
import sogeti.carleaseleaseapi.model.Proposal;
import sogeti.carleaseleaseapi.service.ProposalService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProposalController implements V1Api {

    private final ProposalMapper proposalMapper;
    private final ProposalService proposalService;

    @SneakyThrows
    @Override
    public ResponseEntity<ProposalResponse> createProposalV1(@Valid ProposalAddRequest proposalAddRequest) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String authToken = request.getHeader("authorization");

        Proposal proposal = proposalMapper.mapProposalAddRequestToProposal(proposalAddRequest);
        Proposal addedProposal = proposalService.add(proposal, authToken);
        ProposalResponse proposalResponse = proposalMapper.mapProposalToProposalResponse(addedProposal);
        return ResponseEntity.ok(proposalResponse);
    }

    @Override
    public ResponseEntity<Void> deleteproposalByIdV1(Long proposalId) {
        proposalService.delete(proposalId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<ProposalResponse> getProposalByIdV1(Long proposalId) {
        Proposal proposal = proposalService.retrieve(proposalId);
        ProposalResponse proposalResponse = proposalMapper.mapProposalToProposalResponse(proposal);
        return ResponseEntity.ok(proposalResponse);
    }

    @Override
//    @PreAuthorize("ADMIN")
    public ResponseEntity<List<ProposalResponse>> listProposalsV1() {
        List<Proposal> proposals = proposalService.retrieveAll();
        List<ProposalResponse> proposalResponses = proposals.stream()
                .map(proposalMapper::mapProposalToProposalResponse)
                .toList();
        return ResponseEntity.ok(proposalResponses);
    }

    @Override
    public ResponseEntity<ProposalResponse> updateProposalV1(Long proposalId, @Valid ProposalUpdateRequest proposalUpdateRequest) {
        Proposal proposal = proposalMapper.mapProposalUpdateRequestToProposal(proposalUpdateRequest);
        Proposal updatedProposal = proposalService.update(proposalId, proposal);
        ProposalResponse proposalResponse = proposalMapper.mapProposalToProposalResponse(updatedProposal);
        return ResponseEntity.ok(proposalResponse);
    }
}
