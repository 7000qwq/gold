package gold.service.impl;

import gold.constant.MessageConstant;
import gold.context.BaseContext;
import gold.dto.UserLoginDTO;
import gold.dto.UserModifyDTO;
import gold.dto.UserSignupDTO;
import gold.entity.User;
import gold.exception.AccountNotFoundException;
import gold.exception.PasswordErrorException;
import gold.mapper.UserMapper;
import gold.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void signup(UserSignupDTO userSignupDTO) {

        // insert into user
        User user = new User();
        BeanUtils.copyProperties(userSignupDTO, user);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        userMapper.insert(user);


    }

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();

        User user = userMapper.getByEmail(email);

        if (user == null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        return user;
    }

    @Override
    public User getUserById(Long currentId) {
        User user = userMapper.getById(currentId);
        return user;
    }

    @Override
    public void update(UserModifyDTO userModifyDTO) {

        User user = new User();
        BeanUtils.copyProperties(userModifyDTO, user);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setId(BaseContext.getCurrentId());
        userMapper.update(user);
    }
}
