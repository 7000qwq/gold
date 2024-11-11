package gold.mapper;

import gold.entity.MinutePrice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface GoldPriceMapper {

    @Insert("insert into minute_price (time, gold_price) values (#{time}, #{goldPrice})")
    void insert(MinutePrice minutePrice);

    @Select("SELECT gold_price FROM minute_price ORDER BY id DESC LIMIT 1")
    BigDecimal getNewestPrice();
}
