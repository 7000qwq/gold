package gold.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PositionVO implements Serializable {

    private BigDecimal weight;

    private BigDecimal money;
}
