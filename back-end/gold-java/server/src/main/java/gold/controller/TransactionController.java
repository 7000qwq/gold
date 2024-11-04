package gold.controller;

import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.result.PageResult;
import gold.result.Result;
import gold.vo.TransactionQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

    @PostMapping
    public Result insert(@RequestBody TransactionAddDTO transactionAddDTO){

        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody TransactionModifyDTO transactionModifyDTO){

        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TransactionQueryVO> getTransById(@PathVariable Long id){

        TransactionQueryVO transactionQueryVO = new TransactionQueryVO();
        return Result.success(transactionQueryVO);
    }

    @DeleteMapping("/{id}")
    public Result deleteTransById(@PathVariable Long id){

        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(@RequestBody TransactionPageQueryDTO transactionPageQueryDTO){

        PageResult pageResult = new PageResult();
        return Result.success(pageResult);
    }
}
