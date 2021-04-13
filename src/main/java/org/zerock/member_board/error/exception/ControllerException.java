package org.zerock.member_board.error.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class ControllerException extends RuntimeException{

    private String code;

    private String path;

    public ControllerException(String code, String path) {
        this.code = code;
        this.path = path;
    }
}
