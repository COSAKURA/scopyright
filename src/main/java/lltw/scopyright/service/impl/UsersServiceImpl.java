package lltw.scopyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Users;
import lltw.scopyright.form.LoginForm;
import lltw.scopyright.form.RegisterFrom;
import lltw.scopyright.mapper.UsersMapper;
import lltw.scopyright.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lltw.scopyright.utils.JJWT;
import lltw.scopyright.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sakura
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    private final UsersMapper usersMapper;
    private final JJWT genjwt;

    @Autowired
    public UsersServiceImpl(UsersMapper usersMapper , JJWT genjwt) {
        this.usersMapper = usersMapper;
        this.genjwt = genjwt;
    }

    private final SHA256 sha256 = new SHA256();

    @Override
    public ResultVO login(LoginForm loginForm) throws NoSuchAlgorithmException {
        // 1. 判断用户是否存在（username）
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", loginForm.getEmail())
                .eq("role", loginForm.getRole());

        Users user = usersMapper.selectOne(queryWrapper);

        if (user == null) {
            // 邮箱不存在或角色不匹配
            return ResultVO.error(-1,"邮箱不存在或角色不匹配");
        } else {
            // 2. 判断密码是否正确（password）
            // 从数据库中获取用户的盐值和哈希密码
            byte[] salt = SHA256.base64ToSalt(user.getSalt());
            String storedHash = user.getPassword();

            // 验证密码
            boolean isPasswordCorrect = sha256.verifyPassword(loginForm.getPassword(), storedHash, salt);

            if (!isPasswordCorrect) {
                // 密码不匹配
                return ResultVO.error(-2,"密码不正确");
            }
        }
        // 登录成功，生成jwt令牌
        // Map<String, Object> claims = new HashMap<>();
        // claims.put("id",user.getId());
        // claims.put("name",user.getUsername());
        // claims.put("role",user.getRole());
        // String jwt = genjwt.genJwt(claims);
        Map<String, Object> data = new HashMap<>();
        data.put("username",user.getUsername());
        data.put("role",user.getRole());
        data.put("id",user.getId());
        return ResultVO.success(data);
    }

    @Override
    public ResultVO register(RegisterFrom registerFrom) throws NoSuchAlgorithmException {
        // 1. 验证用户名是否存在
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", registerFrom.getEmail());
        Users user = usersMapper.selectOne(queryWrapper);

        if (user!= null) {
            // 邮箱已存在
            return ResultVO.error(-1,"邮箱已存在");
        }

        // 3. 注册新用户
        byte[] salt = SHA256.generateSalt();
        String hashedPassword = sha256.hashPassword(registerFrom.getPassword(), salt);

        Users newUser = new Users();
        newUser.setUsername(registerFrom.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setSalt(SHA256.saltToBase64(salt));
        newUser.setEmail(registerFrom.getEmail());
        newUser.setRole(registerFrom.getRole());
        usersMapper.insert(newUser);
        return ResultVO.success();

    }
}
