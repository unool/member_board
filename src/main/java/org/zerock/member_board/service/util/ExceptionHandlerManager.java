package org.zerock.member_board.service.util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zerock.member_board.dto.ErrorResponse;
import org.zerock.member_board.error.exception.ControllerException;


@ControllerAdvice
@Slf4j
public class ExceptionHandlerManager {

    @ExceptionHandler(ControllerException.class)
    protected  ResponseEntity<ErrorResponse> handleControllerException(ControllerException e)
    {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("["+ e.getPath() + "|" + e.getCode() + "] error");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
