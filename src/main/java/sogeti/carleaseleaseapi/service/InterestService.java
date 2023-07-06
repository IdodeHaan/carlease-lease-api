package sogeti.carleaseleaseapi.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import sogeti.carleaseleaseapi.exceptionhandling.DuplicateStartDateException;
import sogeti.carleaseleaseapi.exceptionhandling.ResourceNotFoundException;
import sogeti.carleaseleaseapi.model.Interest;
import sogeti.carleaseleaseapi.repository.InterestRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class InterestService {

    private final InterestRepository interestRepository;


    public Interest add(Interest interest) throws DuplicateStartDateException {
        Interest existingInterest = interestRepository.getInterestByStartDate(interest.getStartDate());
        if (existingInterest != null) {
            throw new DuplicateStartDateException("There already exists an interest with given start date");
        }
        interest.setId(0L);
        return interestRepository.save(interest);
    }
    public Interest retrieve(LocalDate startDate) {
        List<Interest> rates = interestRepository.getInterestsByStartDateIsLessThanEqualOrderByStartDateDesc(startDate);
        if (rates.isEmpty()) {
            throw new ResourceNotFoundException("There is no interest rate for this date");
        }
        return rates.get(0);
    }

    public List<Interest> retrieveAll() {
        List<Interest> interests = interestRepository.findAll();
        if (interests.isEmpty()) {
            throw new ResourceNotFoundException("No interest rates stored");
        }
        return interests;
    }

    public void delete(Long id) {
        Optional<Interest> _interest = interestRepository.findById(id);
        _interest.orElseThrow(() -> new ResourceNotFoundException("To be deleted interest not found - id " + id));
        interestRepository.deleteById(id);
    }
}
