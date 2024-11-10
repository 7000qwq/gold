package gold.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionAddDTO implements Serializable {

    private Long id;

    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    private Integer type;
    // 0 - buy      1 - sell

    private BigDecimal goldPrice;

    private BigDecimal amount;

    private BigDecimal weight;

    private BigDecimal commission;

    private String note;
}
