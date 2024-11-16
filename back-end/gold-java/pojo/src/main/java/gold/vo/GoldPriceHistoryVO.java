package gold.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoldPriceHistoryVO implements Serializable {

    //日期时刻，以逗号分隔，例如：2022-10-01 23:22:13,2022-10-02 16:35:19
    private String timeList;

    //金价，以逗号分隔，例如：406.0,1520.0
    private String priceList;

}
