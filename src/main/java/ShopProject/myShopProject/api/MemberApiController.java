package ShopProject.myShopProject.api;

import ShopProject.myShopProject.Domain.Address;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.api.dto.MemberDto;
import ShopProject.myShopProject.api.dto.createMemberRequest;
import ShopProject.myShopProject.api.dto.createMemberResponse;
import ShopProject.myShopProject.api.dto.Result;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 맴버 회원 가입 요청을 dto로 받아들이고, 반환결과인 id를 dto로 반환한다.
    @PostMapping("/api/members")
    public createMemberResponse saveMember(@RequestBody
                                           @Valid createMemberRequest request) {
        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        Member member = new Member();
        member.setAddress(address);
        member.setName(request.getName());
        member.setLoginId(request.getLoginId());
        member.setPassword(request.getPassword());
        Long id = memberService.join(member);
        return new createMemberResponse(id);
    }

    // 맴버 조회 요청을 dto로 받아들이고, 반환 결과인 id를 dto로 반환한다.
    @GetMapping("/api/members")
    public Result getmembers() {
        List<Member> memberList = memberService.findAll();

        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getLoginId(), m.getPassword(),
                        m.getName(), m.getAddress()))
                .collect(Collectors.toList());


        return new Result(collect);
    }

    //
    @GetMapping("/api/members/{id}")
    public MemberDto getmemberById(@PathVariable("id") Long request) {
        Member findMember = memberService.findOne(request);
        return new MemberDto(findMember.getLoginId(), findMember.getPassword(), findMember.getName(), findMember.getAddress());
    }


}
