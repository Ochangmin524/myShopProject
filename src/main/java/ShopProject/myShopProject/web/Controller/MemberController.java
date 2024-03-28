package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.web.Form.MemberForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    //회원목록
    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    //회원 가입 접근
    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    //회원 가입 처리
    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Member member = memberService.createMember(form);
        Long loginMemberId = memberService.join(member);

        if (loginMemberId == null) {
            result.addError(new FieldError("loginId","loginId","이미 존재하는 ID 입니다."));
            return "/members/createMemberForm";
        }
        return "redirect:/";
    }


}
