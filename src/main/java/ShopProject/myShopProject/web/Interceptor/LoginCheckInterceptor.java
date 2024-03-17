package ShopProject.myShopProject.web.Interceptor;


import ShopProject.myShopProject.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws Exception {
        log.info("로그인 체크 인터셉트");

        String requestURI = request.getRequestURI();
        log.info("경로"+ requestURI);
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {

            log.info("미로그인 요청");
            //login으로 리다이렉트, 쿼리파라미터로 redirect경로를 넣어서 요청앴던 경로로
            //갈 수 있도록 한다.
            response.sendRedirect("/login?redirectURL=" +requestURI);
            return false;
        }
        return true;


    }
}
