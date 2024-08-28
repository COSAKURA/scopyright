package lltw.scopyright.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sakura
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO {
    /**
     * 实体类
     */
    private int code;
    private String msg;
    private Object data;

    /**
     *请求成功，返会数据
     */
    public static ResultVO success(Object data){
        return new ResultVO(200,"success",data);
    }

    /**
     *请求成功，不返回数据
     */
    public static ResultVO success(){
        return new ResultVO(200,"success",null);
    }

    /**
     *请求失败，返回报错信息
     */
    public static ResultVO error(int code , String msg){
        return new ResultVO(code,msg,null);
    }

}
