package gold.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TransactionPageQueryDTO implements Serializable {

    private Long userId;

    private int page;

    private int pageSize;

    private Integer type;
    // 0 - buy      1 - sell

    private String stringBeginTime;

    private String stringEndTime;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
