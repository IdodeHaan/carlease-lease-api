package sogeti.carleaseleaseapi.exceptionhandling;

import lombok.Data;

@Data
public class ExceptionResponse {
    private int status;
    private String message;
    private long timestamp;
}
