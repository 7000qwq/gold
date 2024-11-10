package gold.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserStrategyModifyDTO implements Serializable {

    private String note;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private Integer emailNotification;
    // 0 - no send     1 - send
}
