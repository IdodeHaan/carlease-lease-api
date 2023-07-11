package sogeti.carleaseleaseapi.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "carlease-customer", url = "localhost:8082")
public interface CarLeaseCustomerProxy {

    @GetMapping("/v1/customers/{customerId}")
    ResponseEntity<CustomerResponse> getCustomerByIdV1(@PathVariable Long customerId,
                                                       @RequestHeader("Authorization") String bearerToken);
}
