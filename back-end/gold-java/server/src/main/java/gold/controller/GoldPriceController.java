package gold.controller;

import gold.context.BaseContext;
import gold.result.Result;
import gold.service.GoldPriceService;
import gold.service.TransactionService;
import gold.vo.GoldPriceHistoryVO;
import gold.vo.GoldPriceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/goldPrice")
@Slf4j
public class GoldPriceController {

    @Autowired
    private GoldPriceService goldPriceService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/realTime")
    public Result<GoldPriceVO> realtime() throws IOException, InterruptedException {
        GoldPriceVO goldPriceVO = new GoldPriceVO();
        goldPriceVO.setGoldPrice(goldPriceService.newestPrice());
        goldPriceVO.setTime(LocalDateTime.now());

        return Result.success(goldPriceVO);
    }

    @GetMapping("/report")
    public Result<GoldPriceHistoryVO> report(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")LocalDateTime beginTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")LocalDateTime endTime
            ){

        GoldPriceHistoryVO goldPriceHistoryVO = new GoldPriceHistoryVO();
        return Result.success(goldPriceHistoryVO);
    }

    @GetMapping("/position")
    public Result<BigDecimal> position(){

        BigDecimal weight = transactionService.position(BaseContext.getCurrentId());
        log.info("持仓重量为:{}", weight);
        return Result.success(weight);
    }
}
