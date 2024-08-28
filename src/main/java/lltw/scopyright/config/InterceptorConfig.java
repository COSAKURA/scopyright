package lltw.scopyright.config;

import lltw.scopyright.interceptor.LoginCheckedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author Sakura
 */
// 注册拦截器
// @Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    private final LoginCheckedInterceptor loginCheckedInterceptor;
    // @Autowired
    public InterceptorConfig(LoginCheckedInterceptor loginCheckedInterceptor) {
        this.loginCheckedInterceptor = loginCheckedInterceptor;
    }


    // 注册拦截方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckedInterceptor).addPathPatterns("/**");
    }
}
