package org.zerock.member_board.service.util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zerock.member_board.dto.ErrorResponse;
import org.zerock.member_board.error.exception.ControllerException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(ControllerException.class)
    protected  ResponseEntity<ErrorResponse> handleControllerException(ControllerException e)
    {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("["+ e.getPath() + "|" + e.getCode() + "] error");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(
//            MethodArgumentNotValidException exception) {
//
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setMessage("["+ exception.getMessage()+ "] error");
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//
//    }

//    파라미터가 RestRequest일때만 가능
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> MethodArgumentNotValidException(
//            MethodArgumentNotValidException exception) {
//
//        System.out.println("방문");
//        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return ResponseEntity.badRequest().body(errors);
//    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<Map<String, String>> BindException(
//            MethodArgumentNotValidException exception) {
//
//        System.out.println("방문2");
//        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<Map<String, String>> IllegalStateException(
//            MethodArgumentNotValidException exception) {
//
//        System.out.println("방문2");
//        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return ResponseEntity.badRequest().body(errors);
//    }
}
