package gold.service;

import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import gold.result.PageResult;
import gold.vo.GoldPriceHistoryVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionService {

    void insert(TransactionAddDTO transactionAddDTO);

    PageResult page(TransactionPageQueryDTO transactionPageQueryDTO);

    void delete(Long id);

    Transaction getTransactionById(Long id);

    void update(TransactionModifyDTO transactionModifyDTO);

    BigDecimal position(Long currentId);

    GoldPriceHistoryVO report(LocalDateTime beginTime, LocalDateTime endTime);
}
