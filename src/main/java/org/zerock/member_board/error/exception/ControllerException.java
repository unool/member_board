package org.zerock.member_board.error.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ControllerException extends RuntimeException{

    private String code;

    private String path;
}
