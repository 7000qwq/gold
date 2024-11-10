package gold.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSignupDTO implements Serializable {

    private Long id;

    private String email;

    private String password;

    private String phone;

    private String username;
}
