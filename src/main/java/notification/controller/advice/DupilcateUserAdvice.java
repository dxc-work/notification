package notification.controller.advice;

import notification.exception.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class DupilcateUserAdvice {
    @ResponseBody
    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String duplicateUserHandler(DuplicateUserException ex) {
        return ex.getMessage();
    }
}