package sogeti.carleaseleaseapi.service;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sogeti.carleaseleaseapi.exceptionhandling.ResourceNotFoundException;
import sogeti.carleaseleaseapi.model.Car;
import sogeti.carleaseleaseapi.model.Proposal;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//Todo make tests test only service layer, so no integration test
class ProposalServiceTest {

    @Autowired
    private ProposalService proposalService;
    @MockBean
    private CarService carService;
    @Mock
    private RestTemplate restTemplate;
    private Proposal proposal;


    @BeforeEach
    void setUp() {
        proposal = Proposal.builder()
                .id(0L)
                .customerId(44)
                .car(Car.builder()
                        .id(132)
                        .make("someMake")
                        .model("someModel")
                        .version("someVersion")
                        .numberOfDoors(9)
                        .co2Emission(9)
                        .grossPrice(new BigDecimal("33000"))
                        .nettPrice(new BigDecimal("28000"))
                        .build())
                .mileage(28000)
                .duration(48)
                .startDate(LocalDate.parse("2023-05-01"))
                .effectuated(false)
                .build();
        when(carService.calculateLeaseRate(anyLong(), any())).thenReturn(new BigDecimal("234.56"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAdd_proposalIsCreatedAndLeaseRateIsCalculated_whenCorrectDetailsAreProvided() throws Exception {
        //given
        restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(ResponseEntity.ok(null));
        //when
        Proposal createdProposal = proposalService.add(proposal, "mockAuthToken");
        //then
        assertThat(createdProposal.getId()).isGreaterThan(0L);
        assertThat(createdProposal.getLeaseRate()).isEqualTo(new BigDecimal("234.56"));
    }

    @Test
    void testAdd_throwsResourceNotFoundException_whenCustomerIsNotFound() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.appendField("status", "404");
        when(restTemplate.getForEntity(any(), any())).thenReturn(new ResponseEntity(jsonObject, HttpStatus.NOT_FOUND));
        //when & then
        assertThatThrownBy(() -> proposalService.add(proposal, "mockAuthToken")).isInstanceOf(ResourceNotFoundException.class);
    }
}