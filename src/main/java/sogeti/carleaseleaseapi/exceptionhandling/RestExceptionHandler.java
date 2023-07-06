package sogeti.carleaseleaseapi.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({DuplicateStartDateException.class, ResourceNotFoundException.class, Exception.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        HttpStatus status;
        ExceptionResponse exceptionResponse;
        if (exception instanceof DuplicateStartDateException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        exceptionResponse = createExceptionResponse(status.value(), exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    private ExceptionResponse createExceptionResponse(int status, String message) {
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(status);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}
