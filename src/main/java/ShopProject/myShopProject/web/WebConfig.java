package ShopProject.myShopProject.web;

import ShopProject.myShopProject.web.Interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/","/api/**", "/members/new", "/login/**", "/logout",
                        "/css/**", "/*.ico", "/error");
    }
}
