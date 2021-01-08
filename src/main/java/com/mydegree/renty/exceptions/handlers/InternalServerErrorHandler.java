package com.mydegree.renty.exceptions.handlers;

import com.mydegree.renty.exceptions.InternalServerError;
import com.mydegree.renty.exceptions.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InternalServerErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InternalServerError.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleInternalServerError(InternalServerError e) {
        return new ErrorDTO(e.getMessage());
    }
}
