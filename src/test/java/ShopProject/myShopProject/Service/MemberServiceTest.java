package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Address;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    public void  joinTest() {
     //given
        Member member = createMember();

        //when
        memberService.join(member);

        //then
        Assertions.assertEquals(4,memberRepository.findAll().size()
        ,"회원의 크기가 1이어야 한다.");
        //기존 회원 3명에 추가로 1명 더했다.


    }

    private Member createMember() {
        Address address = new Address("서울","거리", "zipcoded");
        Member member = new Member();
        member.setLoginId("test222");
        member.setPassword("test222");
        member.setName("이름입니다222");
        member.setAddress(address);
        return member;
    }



}