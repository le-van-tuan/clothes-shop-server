package vn.triumphstudio.clothesshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.triumphstudio.clothesshop.domain.response.ApiResponse;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BusinessLogicException.class)
    public ResponseEntity<ApiResponse> handleBusinessLogicException(BusinessLogicException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getErrorMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
