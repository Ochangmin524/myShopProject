package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Address;
import ShopProject.myShopProject.Domain.Cart;
import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Domain.LikedItem;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.CartRepositoryJPA;
import ShopProject.myShopProject.Repository.LikeRepository;
import ShopProject.myShopProject.Repository.MemberRepository;
import ShopProject.myShopProject.web.Form.MemberForm;
import ShopProject.myShopProject.web.Form.MemberKakaoForm;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true) /// 아직 이해하지 못함
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    //회원 가입
    @Transactional //변경
    public Long join(Member member) {
        if(isLoginIdDuplicate(member)){
            return null;
        }
        Cart cart = cartService.createCart();
        member.setCart(cart);
        memberRepository.save(member);

        return member.getId();
    }

    @Transactional
    public void removeMember(Member member) {
        memberRepository.removeMember(member);
    }




    //좋아요 아이템이 아니면 false 반환
    @Transactional
    public Boolean isliked(Member member, Item item) {
        log.info("좋아요 확인 메서드 실행");
//
        log.info("멤버 정보" + member.getName() + " " + member.getLoginId());

        for (LikedItem likeditem: memberRepository.findLikedItems(member)) {
            if (likeditem.getItem().getName().equals(item.getName())) {
                log.info("좋아요인 경우");
                return true;
            }

        }
        log.info("좋아요 아닌 경우");
        return false;
    }

    // 좋아요 -> 좋아요 취소 / 좋아요x -> 좋아요
    @Transactional
    public void likes(Member mem, Item item) {
        log.info("좋아요 메서드 실행");
        // 좋아요 -> 좋아요 취소
        Member member = memberRepository.findMemberwithLikedItems(mem);
        log.info("좋아요 멤버 " + member.getName());
        log.info("좋아요 목록" + member.getLikedItems().toString());
        if (isliked(member, item)) {
            for (LikedItem likedItem : member.getLikedItems()) {

                if (likedItem.getItem().getId().equals(item.getId())) {
                    log.info("좋아요 아이템 조회 성공");
                    member.getLikedItems().remove(likedItem);
                    likeRepository.removeLikedItem(likedItem);
                    cancelLikes(item);
                    break;
                }
            }
        }
        // 좋아요x -> 좋아요
        else {
            log.info("좋아요아이템 생성");
            LikedItem likedItem = new LikedItem();
            log.info("여기에서는 "+item);
            likedItem.setItem(item);
            likedItem.setMember(member);
            likeRepository.addLikedItem(likedItem);
            member.getLikedItems().add(likedItem);
            likesItem(item);
        }
    }

    private void cancelLikes(Item item) {
        item.setLikes(item.getLikes()-1);
    }

    private void likesItem(Item item) {
        item.setLikes(item.getLikes()+1);
    }


    //폼으로 맴버 생성
    @Transactional
    public Member createMember(MemberForm form) {
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setAddress(address);
        member.setName(form.getName());
        member.setLoginId(form.getLoginId());
        member.setPassword(form.getPassword());


        return member;
    }

    //폼으로 맴버 생성 카카오 로그인인 경우
    @Transactional
    public Member createKakaoMember(MemberKakaoForm form) {
        Member member = new Member();
        member.setName(form.getName());
        member.setLoginId(form.getLoginId());
        member.setPassword("K");
        return member;

    }

    // id 중복이면 true 반환
    private boolean isLoginIdDuplicate(Member member) {
        Optional<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if(!findMembers.isEmpty()){
            return true;
        }
        return false;
    }



    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NoSuchElementException("No member found with loginId: " + loginId));
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
