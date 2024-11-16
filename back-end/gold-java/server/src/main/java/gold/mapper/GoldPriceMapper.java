package gold.mapper;

import gold.entity.MinutePrice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GoldPriceMapper {

    @Insert("insert into minute_price (time, gold_price) values (#{time}, #{goldPrice})")
    void insert(MinutePrice minutePrice);

    @Select("select time, gold_price from minute_price where time >= #{beginTime} and time <= #{endTime}")
    List<MinutePrice> getTransactionByTime(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("SELECT gold_price FROM minute_price WHERE gold_price != 0.0 ORDER BY id DESC LIMIT 1")
    BigDecimal getNewestPrice();
}
