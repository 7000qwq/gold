package gold.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DayPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime time;

    private BigDecimal goldPrice;
}
