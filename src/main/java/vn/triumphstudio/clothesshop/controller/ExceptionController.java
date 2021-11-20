package vn.triumphstudio.clothesshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.triumphstudio.clothesshop.domain.response.ErrorResponse;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex) {
        ErrorResponse response = new ErrorResponse();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setCode(ex.getErrorCode());
        response.setMessage(ex.getErrorMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
