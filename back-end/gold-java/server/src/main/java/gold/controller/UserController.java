package gold.controller;

import gold.dto.UserLoginDTO;
import gold.dto.UserModifyDTO;
import gold.dto.UserSignupDTO;
import gold.dto.UserStrategyModifyDTO;
import gold.result.Result;
import gold.vo.UserLoginVO;
import gold.vo.UserSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){

        UserLoginVO userLoginVO = new UserLoginVO();
        return Result.success(userLoginVO);
    }

    @PostMapping("/logout")
    public Result logout(){

        return Result.success();
    }

    @PostMapping("/signup")
    public Result signup(@RequestBody UserSignupDTO userSignupDTO){

        return Result.success();
    }

    @PutMapping
    public Result updateUser(@RequestBody UserModifyDTO userModifyDTO){

        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<UserSearchVO> getUserById(@PathVariable Long id){

        UserSearchVO userSearchVO = new UserSearchVO();
        return Result.success(userSearchVO);
    }

    @PutMapping("/strategy")
    public Result updateStrategy(@RequestBody UserStrategyModifyDTO userStrategyModifyDTO){

        return Result.success();
    }
}
