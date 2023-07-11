package sogeti.carleaseleaseapi.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum AddressType {
    HOME("HOME"),
    CORRESPONDENCE("CORRESPONDENCE"),
    WORK("WORK");

    private String value;
}
