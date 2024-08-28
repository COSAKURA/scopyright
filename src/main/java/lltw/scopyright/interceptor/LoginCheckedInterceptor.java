package lltw.scopyright.interceptor;

import com.alibaba.fastjson.JSONObject;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.utils.JJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sakura
 */

// @Component
public class LoginCheckedInterceptor implements HandlerInterceptor {


    // 目标资源方法执行前执行 true：放行 false：拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求路径
        String url = request.getRequestURI();

        // 判断请求路径是否包含 login
        if (url.contains("/login")) {
            // 若是登录请求，放行
            return true;
        }

        // 获取 Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.hasLength(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            ResultVO notLogin = ResultVO.error(401, "NOT_LOGIN");
            String jsonString = JSONObject.toJSONString(notLogin);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);
            return false;
        }

        // 提取 token 部分
        String token = authorizationHeader.substring(7);

        try {
            JJWT.parseJwt(token);
        } catch (Exception e) {
            e.printStackTrace();
            ResultVO notLogin = ResultVO.error(401, "NOT_LOGIN");
            String jsonString = JSONObject.toJSONString(notLogin);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);
            return false;
        }
        return true;
    }

    // 目标资源方法执行后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 请求处理后调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
