package ShopProject.myShopProject.web.Form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MemberKakaoForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;
    @NotEmpty
    private String loginId;
}
