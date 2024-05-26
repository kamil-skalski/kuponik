package pl.kuponik.infrastructure.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kuponik.domain.exception.CouponNotActiveException;
import pl.kuponik.application.exception.CouponNotFoundException;
import pl.kuponik.domain.exception.InsufficientPointsException;
import pl.kuponik.application.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.domain.exception.UnauthorizedCouponAccessException;
import pl.kuponik.infrastructure.api.dto.ApiErrorResponse;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = LoyaltyAccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(LoyaltyAccountNotFoundException ex) {
        var apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CouponNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CouponNotFoundException ex) {
        var apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InsufficientPointsException.class)
    public ResponseEntity<ApiErrorResponse> handleException(InsufficientPointsException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CouponNotActiveException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CouponNotActiveException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UnauthorizedCouponAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleException(UnauthorizedCouponAccessException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String aggregatedErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var apiErrorResponse = new ApiErrorResponse(aggregatedErrors);
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
