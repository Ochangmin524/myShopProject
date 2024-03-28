package ShopProject.myShopProject.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class createMemberRequest {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
