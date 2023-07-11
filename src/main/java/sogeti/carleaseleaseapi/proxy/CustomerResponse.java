package sogeti.carleaseleaseapi.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private @Valid List<AddressResponse> addresses = null;
}
