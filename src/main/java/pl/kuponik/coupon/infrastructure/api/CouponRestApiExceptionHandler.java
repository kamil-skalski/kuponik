package pl.kuponik.coupon.infrastructure.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kuponik.common.ApiErrorResponse;
import pl.kuponik.coupon.application.exception.CouponNotFoundException;
import pl.kuponik.coupon.domain.exception.CouponNotActiveException;
import pl.kuponik.coupon.domain.exception.UnauthorizedCouponAccessException;

@ControllerAdvice
class CouponRestApiExceptionHandler {

    @ExceptionHandler(value = CouponNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CouponNotFoundException ex) {
        var apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
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
}
