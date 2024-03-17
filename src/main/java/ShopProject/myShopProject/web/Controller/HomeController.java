package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Slf4j
public class HomeController {
    //@SessionAttribute(new = SessionConst.LGOIN_MEMBER , required = false)
    //가 session을 찾고, 세션으로 member를 조회하는 과정을 자동화 해준다.
    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember, Model model) {
        //로그인 여부 확인
        //로그인 하지 않으면 home으로
        if (loginMember == null) {
            return "home/home";
        }

        if (loginMember.getLoginId().equals("admin")) {
            log.info("관리자 로그인");
            return "home/adminHome";
        }
        //로그인한 회원이면 loginHome 으로
        model.addAttribute(loginMember);
        return "home/loginHome";
    }


}
