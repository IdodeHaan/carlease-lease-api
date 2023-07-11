package sogeti.carleaseleaseapi.proxy;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private AddressType type;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String place;
}
