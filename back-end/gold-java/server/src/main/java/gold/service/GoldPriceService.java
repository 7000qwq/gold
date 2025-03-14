package gold.service;

import java.io.IOException;
import java.math.BigDecimal;

public interface GoldPriceService {

    BigDecimal newestPrice() throws IOException, InterruptedException;
    BigDecimal getCurrentGoldPrice() throws IOException, InterruptedException;

    BigDecimal newestJDPrice() throws IOException, InterruptedException;
}
