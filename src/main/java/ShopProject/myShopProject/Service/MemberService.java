package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) /// 아직 이해하지 못함
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional //변경
    public Long join(Member member) {
        if(isLoginIdDuplicate(member)){
            return null;
        }

        memberRepository.save(member);
        return member.getId();
    }

    // id 중복이면 true 반환
    private boolean isLoginIdDuplicate(Member member) {
        Optional<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if(!findMembers.isEmpty()){
            return true;
        }
        return false;
    }


    //하나만 조회
    public Member findOne(Long id) {
       return memberRepository.findOne(id);
    }

    // 멤버 모두 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
