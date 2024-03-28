package ShopProject.myShopProject.api.dto;

import lombok.Data;

@Data
public class createMemberResponse {
    private Long id;
    public createMemberResponse(Long id) {
        this.id = id;
    }

}
