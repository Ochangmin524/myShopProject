package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Service.KakaoLoginService;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.web.Form.MemberKakaoForm;
import ShopProject.myShopProject.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class kakaoLoginController {
    private String client_id ="5ec10617daa936d308701ea688c7fe79";
    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;

    @GetMapping("/login/callback")
    public String KakaoLoginCallback(@RequestParam("code") String code,
                                     HttpServletRequest request) throws IOException {
        log.info("kakologincallback 시작!");
        String accessToken = kakaoLoginService.getAccessTokenFromKakao(client_id, code);
        HashMap<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

        String id = String.valueOf(userInfo.get("id"));
        try {

            Member loginMember = memberService.findByLoginId(id);
            log.info("db에 존재하는 맴버입니다. 맴버에게 세션 부여");

            HttpSession session = request.getSession(true);
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

            //로그인 성공 후 로그인 폼으로 이동
            log.info("로그인 성공 후 로그인 폼으로 이동");
            return "redirect:/";

        }
        catch(NoSuchElementException e) {
                        //회원 폼에 정보 집어넣기
                log.info("회원 폼에 정보 집어넣기");
                MemberKakaoForm form = new MemberKakaoForm();
                form.setName((String) userInfo.get("nickname"));
                form.setLoginId(String.valueOf(userInfo.get("id")));
                //맴버 생성 - 회원가입 과정
                log.info("회원가입 과정");

                Member member = memberService.createKakaoMember(form);
                Long loginMemberId = memberService.join(member);


                //로그인 성공 처리
                //맴버에게 세션 부여
                log.info("맴버에게 세션 부여");

                HttpSession session = request.getSession(true);
                session.setAttribute(SessionConst.LOGIN_MEMBER, member);

                //로그인 성공 후 로그인 폼으로 이동
                log.info("로그인 성공 후 로그인 폼으로 이동");
                return "redirect:/";
        }
        }

    @GetMapping("/login/withdrawByKakao")
    public String WithdrawByKakao(@SessionAttribute(name = "loginMember") Member loginMember,
                                  HttpServletRequest request) throws IOException {
        log.info("카카오 전용 탈퇴 컨트롤러 호출 완료");
        //카카오에게 회원 연결 끊기 POST 요청 보내기
        kakaoLoginService.withdrawKakaoMemberByAdminKey(loginMember);

        //DB에서 회원 삭제
        memberService.removeMember(loginMember);

        //애플리케이션에서 세션 삭제
        HttpSession session = request.getSession();
        session.invalidate();

        //홈으로 리다이렉트
        return "redirect:/";

    }
}
