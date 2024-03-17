package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;


    @Transactional
    //로그인 성공이면, member, 아니면 null 반환
    public Member login(String loginId, String password) {
        return  memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);


    }



}
