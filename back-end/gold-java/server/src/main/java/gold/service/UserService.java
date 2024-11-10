package gold.service;

import gold.dto.UserLoginDTO;
import gold.dto.UserModifyDTO;
import gold.dto.UserSignupDTO;
import gold.entity.User;

public interface UserService {

    void signup(UserSignupDTO userSignupDTO);

    User login(UserLoginDTO userLoginDTO);

    User getUserById(Long currentId);

    void update(UserModifyDTO userModifyDTO);
}
