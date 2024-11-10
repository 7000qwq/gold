package gold.controller;

import gold.context.BaseContext;
import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import gold.result.PageResult;
import gold.result.Result;
import gold.service.TransactionService;
import gold.vo.TransactionQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Result insert(@RequestBody TransactionAddDTO transactionAddDTO){

        transactionAddDTO.setUserId(BaseContext.getCurrentId());
        transactionService.insert(transactionAddDTO);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody TransactionModifyDTO transactionModifyDTO){

        transactionService.update(transactionModifyDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TransactionQueryVO> getTransById(@PathVariable Long id){

        Transaction transaction = transactionService.getTransactionById(id);
        TransactionQueryVO transactionQueryVO = new TransactionQueryVO();
        BeanUtils.copyProperties(transaction, transactionQueryVO);
        return Result.success(transactionQueryVO);
    }

    @DeleteMapping("/{id}")
    public Result deleteTransById(@PathVariable Long id){

        transactionService.delete(id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(TransactionPageQueryDTO transactionPageQueryDTO){

        if (transactionPageQueryDTO.getStringBeginTime() != "") {
            transactionPageQueryDTO.setBeginTime(LocalDateTime.parse(transactionPageQueryDTO.getStringBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }
        if (transactionPageQueryDTO.getStringEndTime() != "") {
            transactionPageQueryDTO.setEndTime(LocalDateTime.parse(transactionPageQueryDTO.getStringEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }


        transactionPageQueryDTO.setUserId(BaseContext.getCurrentId());
        PageResult pageResult = transactionService.page(transactionPageQueryDTO);
        return Result.success(pageResult);
    }
}
