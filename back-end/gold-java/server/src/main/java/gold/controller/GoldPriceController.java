package gold.controller;

import gold.result.Result;
import gold.service.GoldPriceService;
import gold.vo.GoldPriceHistoryVO;
import gold.vo.PositionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/goldPrice")
@Slf4j
public class GoldPriceController {

    @Autowired
    private GoldPriceService goldPriceService;

    @GetMapping("/realTime")
    public Result<BigDecimal> realTime(){
        BigDecimal price = goldPriceService.getPirce();
        return Result.success(price);
    }

    @GetMapping("/report")
    public Result<GoldPriceHistoryVO> report(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")LocalDateTime beginTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")LocalDateTime endTime
            ){

        GoldPriceHistoryVO goldPriceHistoryVO = new GoldPriceHistoryVO();
        return Result.success(goldPriceHistoryVO);
    }

    @GetMapping("/position/{id}")
    public Result<PositionVO> position(@PathVariable Long id){

        PositionVO positionVO = new PositionVO();
        return Result.success(positionVO);
    }
}
