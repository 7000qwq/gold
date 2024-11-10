package gold.controller;

import gold.context.BaseContext;
import gold.dto.UserLoginDTO;
import gold.dto.UserModifyDTO;
import gold.dto.UserSignupDTO;
import gold.dto.UserStrategyModifyDTO;
import gold.entity.User;
import gold.properties.JwtProperties;
import gold.result.Result;
import gold.service.UserService;
import gold.utils.JwtUtil;
import gold.vo.UserLoginVO;
import gold.vo.UserSearchVO;
import gold.vo.UserStrategyGetVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/signup")
    public Result signup(@RequestBody UserSignupDTO userSignupDTO){

        userService.signup(userSignupDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){

        User user = userService.login(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        userLoginVO.setToken(token);

        log.info(String.valueOf(userLoginVO));

        return Result.success(userLoginVO);
    }

    @PostMapping("/logout")
    public Result logout(){

        return Result.success();
    }

    @PutMapping
    public Result updateUser(@RequestBody UserModifyDTO userModifyDTO){

        userService.update(userModifyDTO);
        return Result.success();
    }

    @GetMapping
    public Result<UserSearchVO> getUserById(){

        User user = userService.getUserById(BaseContext.getCurrentId());
        UserSearchVO userSearchVO = new UserSearchVO();
        BeanUtils.copyProperties(user, userSearchVO);
        return Result.success(userSearchVO);
    }

    @GetMapping("/name")
    public Result<String> getUsername(){

        String username;
        User user = userService.getUserById(BaseContext.getCurrentId());
        if (user.getUsername() != null && !user.getUsername().isEmpty()){
            username = user.getUsername();
        }
        else
            username = user.getEmail();
        // log.info("username:{}", username);
        return Result.success(username);
    }

    @GetMapping("/strategy")
    public Result<UserStrategyGetVO> getStrategyByUserId(){

        Long userId = BaseContext.getCurrentId();
        UserStrategyGetVO userStrategyGetVO = new UserStrategyGetVO();
        userStrategyGetVO.setNote((String) redisTemplate.opsForHash().get(userId, "note"));
        userStrategyGetVO.setHighPrice((BigDecimal) redisTemplate.opsForHash().get(userId, "highPrice"));
        userStrategyGetVO.setLowPrice((BigDecimal) redisTemplate.opsForHash().get(userId, "lowPrice"));
        userStrategyGetVO.setEmailNotification((Integer) redisTemplate.opsForHash().get(userId, "emailNotification"));
        return Result.success(userStrategyGetVO);
    }

    @PutMapping("/strategy")
    public Result updateStrategy(@RequestBody UserStrategyModifyDTO userStrategyModifyDTO){

        Long userId = BaseContext.getCurrentId();

        redisTemplate.opsForHash().put(userId, "note", userStrategyModifyDTO.getNote());
        redisTemplate.opsForHash().put(userId, "highPrice", userStrategyModifyDTO.getHighPrice());
        redisTemplate.opsForHash().put(userId, "lowPrice", userStrategyModifyDTO.getLowPrice());
        redisTemplate.opsForHash().put(userId, "emailNotification", userStrategyModifyDTO.getEmailNotification());

        return Result.success();
    }


}
