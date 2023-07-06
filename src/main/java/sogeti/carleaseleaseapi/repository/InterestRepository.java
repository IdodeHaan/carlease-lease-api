package sogeti.carleaseleaseapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogeti.carleaseleaseapi.model.Interest;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> getInterestsByStartDateIsLessThanEqualOrderByStartDateDesc(LocalDate date);

    Interest getInterestByStartDate(LocalDate date);
}
