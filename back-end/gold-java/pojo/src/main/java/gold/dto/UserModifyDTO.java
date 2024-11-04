package gold.dto;

import lombok.Data;

@Data
public class UserModifyDTO {

    private Long id;

    private String email;

    private String password;

    private String phone;

    private String username;
}
