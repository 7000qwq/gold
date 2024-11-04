package gold.vo;

import lombok.Data;

@Data
public class UserSearchVO {

    private Long id;

    private String email;

    private String password;

    private String phone;

    private String username;
}
