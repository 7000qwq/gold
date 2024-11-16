package gold.mapper;

import com.github.pagehelper.Page;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface TransactionMapper {

    @Insert("insert into history " +
            "(user_id, time, type, gold_price, amount, commission, note, weight)" +
            " values " +
            "(#{userId}, #{time}, #{type}, #{goldPrice}, #{amount}, #{commission}, #{note}, #{weight})")
    void insert(Transaction transaction);

    Page<Transaction> page(TransactionPageQueryDTO transactionPageQueryDTO);

    @Delete("delete from history where id = #{id}")
    void delete(Long id);

    @Select("select * from history where id = #{id}")
    Transaction getTransactionById(Long id);

    @Update("update history set " +
            "user_id = #{userId}, time = #{time}, type = #{type}, gold_price = #{goldPrice}, " +
            "amount = #{amount}, commission = #{commission}, note = #{note}, weight = #{weight}" +
            "WHERE id = #{id}")
    void update(Transaction transaction);

    BigDecimal position(Long currentId);
}
