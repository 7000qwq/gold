package gold.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchVO implements Serializable {

    private String email;

    private String password;

    private String phone;

    private String username;
}
