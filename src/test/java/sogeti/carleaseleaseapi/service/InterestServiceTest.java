package sogeti.carleaseleaseapi.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogeti.carleaseleaseapi.exceptionhandling.DuplicateStartDateException;
import sogeti.carleaseleaseapi.exceptionhandling.ResourceNotFoundException;
import sogeti.carleaseleaseapi.model.Interest;
import sogeti.carleaseleaseapi.repository.InterestRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class InterestServiceTest {

    @Autowired
    InterestService interestService;

    @Autowired
    InterestRepository interestRepository;
    private BigDecimal rate = new BigDecimal("3.55");
    private LocalDate startDate = LocalDate.parse("2023-05-31");
    private Interest savedInterest;

    @BeforeEach
    void setup() throws DuplicateStartDateException {
        //given
        interestRepository.deleteAll();
        Interest interest = new Interest();
        interest.setRate(rate);
        interest.setStartDate(startDate);
        //when
        savedInterest = interestService.add(interest);
    }

    @AfterEach
    void cleanup() {
        interestRepository.delete(savedInterest);
    }

    @Test
    void testAdd_GivenCorrectDetails_InterestObjectWithIdCreated() {
        //then
        assertThat(savedInterest.getId()).isGreaterThan(0);
        assertThat(savedInterest.getRate()).isEqualTo(rate);
        assertThat(savedInterest.getStartDate()).isEqualTo(startDate);
    }

    @Test
    void testAdd_ExceptionIsThrown_WhenAlreadyARecordExistsWithGivenStartDate() {
        //when  & then
        assertThatThrownBy(() -> interestService.add(savedInterest))
                .isInstanceOf(DuplicateStartDateException.class);
    }

    @Test
    void testRetrieve_retrievesInterest_whenGivenDateIsStartDate() {
        //when
        Interest interest = interestService.retrieve(startDate);
        //then
        assertThat(interest.getStartDate()).isEqualTo(startDate);
    }

    @Test
    void testRetrieve_retrievesInterest_whenGivenDateIsBiggerThanStartDate() {
        //when
        LocalDate dateAfterStartDate = startDate.plusDays(2);
        Interest interest = interestService.retrieve(dateAfterStartDate);
        //then
        assertThat(interest.getStartDate()).isEqualTo(startDate);
    }

    @Test
    void testRetrieve_retrieveNoInterest_whenGivenDateIsSmallerThanStartDate() {
        //when
        LocalDate dateBeforeStartDate = startDate.minusDays(2);
        //when  & then
        assertThatThrownBy(() -> interestService.retrieve(dateBeforeStartDate))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @Transactional
    void testRetrieve_retrieveInterestWithGreatestStartDate_WhenMultipleInterestsHaveStartDateLessThenDate() throws DuplicateStartDateException {
        Interest interestMin2Months = new Interest();
        interestMin2Months.setRate(new BigDecimal("4.20"));
        interestMin2Months.setStartDate(startDate.minusMonths(2));
        savedInterest = interestService.add(interestMin2Months);

        Interest interestMin1Month = new Interest();
        interestMin1Month.setRate(new BigDecimal("4.70"));
        interestMin1Month.setStartDate(startDate.minusMonths(1));
        savedInterest = interestService.add(interestMin1Month);
        //when
        Interest retrievedInterest = interestService.retrieve(startDate.minusDays(10));
        //then
        assertThat(retrievedInterest.getStartDate()).isEqualTo(interestMin1Month.getStartDate());
    }

    @Test
    @Transactional
    void testRetrieveAll_retrieveAll_WhenMultipleInterestsArePresent() throws DuplicateStartDateException {
        Interest interestMin2Months = new Interest();
        interestMin2Months.setRate(new BigDecimal("4.20"));
        interestMin2Months.setStartDate(startDate.minusMonths(2));
        savedInterest = interestService.add(interestMin2Months);

        Interest interestMin1Month = new Interest();
        interestMin1Month.setRate(new BigDecimal("4.70"));
        interestMin1Month.setStartDate(startDate.minusMonths(1));
        savedInterest = interestService.add(interestMin1Month);
        //when
        List<Interest> rates = interestService.retrieveAll();
        //then
        assertThat(rates.size()).isEqualTo(3);
    }
}