package gold.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionQueryVO implements Serializable {

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
