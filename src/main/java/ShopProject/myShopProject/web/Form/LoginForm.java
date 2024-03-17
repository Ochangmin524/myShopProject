package ShopProject.myShopProject.web.Form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginForm {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

}
