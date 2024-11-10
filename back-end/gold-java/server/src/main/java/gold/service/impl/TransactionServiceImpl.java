package gold.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import gold.context.BaseContext;
import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import gold.mapper.TransactionMapper;
import gold.result.PageResult;
import gold.service.TransactionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

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
}
