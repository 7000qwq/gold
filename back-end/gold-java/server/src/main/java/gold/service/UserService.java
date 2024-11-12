package gold.service;

import gold.dto.UserLoginDTO;
import gold.dto.UserModifyDTO;
import gold.entity.User;

public interface UserService {

    void insert(User user);

    User login(UserLoginDTO userLoginDTO);

    User getUserById(Long currentId);

    void update(UserModifyDTO userModifyDTO);
}
