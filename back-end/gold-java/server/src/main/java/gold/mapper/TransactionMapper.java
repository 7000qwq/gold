package gold.mapper;

import com.github.pagehelper.Page;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

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

    @Select("select * from history where user_id = #{userID}")
    List<Transaction> getExcelByUserId(Long userID);

    @Select("SELECT * FROM history " +
            "WHERE user_id = 1 " +
            "AND type = 0 " +
            "AND note = '' " +
            "AND FLOOR(gold_price) = FLOOR(CAST(#{price} AS DECIMAL(10, 2)))")
    List<Transaction> findMatchingRecords(BigDecimal price);

    @Select("SELECT * FROM history " +
            "WHERE user_id = #{userId} " +
            "AND type = 0 " +
            "AND note = '' ")
    List<Transaction> getPositionAllByUserId(Long userId);

    @Update("UPDATE history SET note = 'soldAndBuy' WHERE id = #{id}")
    void markSoldAndBuy(Long id);
}
