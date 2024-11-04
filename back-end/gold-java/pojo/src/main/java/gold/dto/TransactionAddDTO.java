package gold.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionAddDTO {

    private Long id;

    private Long userId;

    private LocalDateTime time;

    private Integer type;
    // 0 - buy      1 - sell

    private BigDecimal goldPrice;

    private BigDecimal amount;

    private BigDecimal commission;

    private String note;
}
