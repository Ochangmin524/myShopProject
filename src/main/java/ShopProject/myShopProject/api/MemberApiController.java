package ShopProject.myShopProject.api;

import ShopProject.myShopProject.Domain.Address;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.api.dto.MemberDto;
import ShopProject.myShopProject.api.dto.createMemberRequest;
import ShopProject.myShopProject.api.dto.createMemberResponse;
import ShopProject.myShopProject.api.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

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
    @GetMapping("/api/members")
    public Result getmembers() {
        List<Member> memberList = memberService.findAll();

        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getLoginId(), m.getPassword(),
                        m.getName(), m.getAddress()))
                .collect(Collectors.toList());


        return new Result(collect);
    }


}
