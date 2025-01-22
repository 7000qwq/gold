package gold.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionHistoryVO implements Serializable {


    //交易金价，以逗号分隔，例如：406.0,1520.0
    private String priceList;

    //交易克重，以逗号分隔，例如：1.7234,2.9483
    private String weightList;

    //日期时刻，以逗号分隔，例如：2022-10-01 23:22:13,2022-10-02 16:35:19
    private String timeList;

}
