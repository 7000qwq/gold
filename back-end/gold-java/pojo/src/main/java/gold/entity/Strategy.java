package gold.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Strategy implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String note;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private Integer emailNotification;
    // 0 - no send     1 - send
}
