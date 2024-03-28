package ShopProject.myShopProject.api.dto;

import ShopProject.myShopProject.Domain.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class MemberDto {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;
    private Address address;

    public MemberDto(String loginId, String password, String name, Address address) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.address = address;

    }
}
