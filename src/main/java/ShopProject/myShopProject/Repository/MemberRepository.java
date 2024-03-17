package ShopProject.myShopProject.Repository;


import ShopProject.myShopProject.Domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {


    @PersistenceContext
    private EntityManager em;

    //회원 저장
    public void save(Member member) { //
        em.persist(member);
    }

    //id로 회원 조회
    public Member findOne(Long id) {
        return em.find(Member.class,id);
    }

    //모든 회원 리스트로 조회
    public List<Member> findAll() {
         return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }

    //회원 로그인ID로 찾기
    public  Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    // 회원 이름으로 찾기
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",
                        Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
