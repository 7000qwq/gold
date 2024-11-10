package gold.service;

import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import gold.result.PageResult;

public interface TransactionService {

    void insert(TransactionAddDTO transactionAddDTO);

    PageResult page(TransactionPageQueryDTO transactionPageQueryDTO);

    void delete(Long id);

    Transaction getTransactionById(Long id);

    void update(TransactionModifyDTO transactionModifyDTO);
}
