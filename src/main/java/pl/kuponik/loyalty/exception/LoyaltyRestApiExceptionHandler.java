package pl.kuponik.loyalty.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kuponik.common.ApiErrorResponse;

@ControllerAdvice
class LoyaltyRestApiExceptionHandler {

    @ExceptionHandler(value = LoyaltyAccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(LoyaltyAccountNotFoundException ex) {
        var apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InsufficientPointsException.class)
    public ResponseEntity<ApiErrorResponse> handleException(InsufficientPointsException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
