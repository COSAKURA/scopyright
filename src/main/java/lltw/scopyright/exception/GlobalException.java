package lltw.scopyright.exception;

import lltw.scopyright.VO.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Sakura
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResultVO globalError (Exception e){
        return ResultVO.error(400,"操作失败，请联系管理员！");
    }
}
