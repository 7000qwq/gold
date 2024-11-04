package gold.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserStrategyModifyDTO {

    private Long userId;

    private BigDecimal longAmount;

    private BigDecimal shortAmount;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private Integer emailNotification;
    // 0 - no send     1 - send
}
