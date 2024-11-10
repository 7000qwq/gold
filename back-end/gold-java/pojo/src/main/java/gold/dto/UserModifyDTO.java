package gold.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserModifyDTO implements Serializable {

    private String email;

    private String password;

    private String phone;

    private String username;
}
