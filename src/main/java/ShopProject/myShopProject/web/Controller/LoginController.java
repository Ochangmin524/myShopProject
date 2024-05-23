package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Service.LoginService;
import ShopProject.myShopProject.web.Form.LoginForm;
import ShopProject.myShopProject.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
            HttpServletRequest request)  {
        log.info("로그인 시작");

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        //로그인
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? = {}", loginMember);


        //로그인 오류
        if (loginMember == null) {
            //글로벌 오류 생성
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }


        //로그인 성공 처리
        //true: 세션이 있으면 기존 세션 반환, 없으면 새로운 세션 생성
        //false: 세션이 있으면 기존 세션 반환, 없으면 null 반환
        HttpSession session = request.getSession(true);

        //로그인한 회원은 session을 들고 있다.
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        log.info("맴버에게 세션 부여");

        //관리자 페이지
        if (loginMember.getLoginId().equals("admin")) {
            log.info("관리자 로그인");
            return "home/adminHome";
        }
        log.info("리다이렉트");
        return "redirect:/";
    }


    @PostMapping("/logout")
    public String  logout(HttpServletRequest request) {
        log.info("로그아웃 ");
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";

    }
}
