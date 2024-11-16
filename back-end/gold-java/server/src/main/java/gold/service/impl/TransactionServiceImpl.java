package gold.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import gold.context.BaseContext;
import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.MinutePrice;
import gold.entity.Transaction;
import gold.mapper.GoldPriceMapper;
import gold.mapper.TransactionMapper;
import gold.result.PageResult;
import gold.service.TransactionService;
import gold.vo.GoldPriceHistoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private GoldPriceMapper goldPriceMapper;

    @Override
    public void insert(TransactionAddDTO transactionAddDTO) {

        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionAddDTO, transaction);
        transactionMapper.insert(transaction);
        return;
    }

    @Override
    public PageResult page(TransactionPageQueryDTO transactionPageQueryDTO) {

        PageHelper.startPage(transactionPageQueryDTO.getPage(), transactionPageQueryDTO.getPageSize());

        Page<Transaction> page = transactionMapper.page(transactionPageQueryDTO);

        long total = page.getTotal();
        List<Transaction> records = page.getResult();

        return new PageResult(total, records);
    }

    @Override
    public void delete(Long id) {
        transactionMapper.delete(id);
        return;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionMapper.getTransactionById(id);
    }

    @Override
    public void update(TransactionModifyDTO transactionModifyDTO) {

        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionModifyDTO, transaction);
        transaction.setUserId(BaseContext.getCurrentId());
        transactionMapper.update(transaction);
    }

    @Override
    public BigDecimal position(Long currentId) {

        BigDecimal weight = transactionMapper.position(currentId);
        return weight;
    }

    @Override
    public GoldPriceHistoryVO report(LocalDateTime beginTime, LocalDateTime endTime) {
        String dateList = "";
        String priceList = "";

        List<MinutePrice> list = goldPriceMapper.getTransactionByTime(beginTime, endTime);

        for (MinutePrice price : list) {
            dateList = dateList + price.getTime().toString() + ",";
            priceList = priceList + price.getGoldPrice().toString() + ",";
        }

        if (dateList.length() > 0) {
            dateList = dateList.substring(0, dateList.length() - 1);
        }
        if (priceList.length() > 0) {
            priceList = priceList.substring(0, priceList.length() - 1);
        }
        GoldPriceHistoryVO goldPriceHistoryVO = new GoldPriceHistoryVO();
        goldPriceHistoryVO.setPriceList(priceList);
        goldPriceHistoryVO.setTimeList(dateList);
        log.info("返回图表数据为:{}", goldPriceHistoryVO);
        return goldPriceHistoryVO;
    }

    @Override
    public List<Transaction> getExcelByUserId(Long userID) {
        return transactionMapper.getExcelByUserId(userID);
    }
}
