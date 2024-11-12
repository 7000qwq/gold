package gold.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginVO implements Serializable {

    private Long id;

    private String username;

    private String token;

    private String URL;
}
