package ShopProject.myShopProject.Repository;


import ShopProject.myShopProject.Domain.LikedItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    @PersistenceContext
    EntityManager em;

    public void addLikedItem(LikedItem likedItem) {
        em.persist(likedItem);
    }

    public void removeLikedItem(LikedItem likedItem) {
        em.remove(em.contains(likedItem) ? likedItem : em.merge(likedItem));
    }
}
