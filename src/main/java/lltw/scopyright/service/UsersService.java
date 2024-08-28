package lltw.scopyright.service;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import lltw.scopyright.form.LoginForm;
import lltw.scopyright.form.RegisterFrom;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@Service
public interface UsersService extends IService<Users> {
    ResultVO login(LoginForm loginForm) throws NoSuchAlgorithmException;

    ResultVO register(RegisterFrom registerFrom) throws NoSuchAlgorithmException;

}
