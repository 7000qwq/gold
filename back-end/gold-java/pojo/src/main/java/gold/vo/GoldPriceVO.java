package gold.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoldPriceVO {
    
    private LocalDateTime time;

    private BigDecimal goldPrice;

}
