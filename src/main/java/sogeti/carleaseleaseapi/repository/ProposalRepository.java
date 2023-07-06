package sogeti.carleaseleaseapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogeti.carleaseleaseapi.model.Proposal;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
