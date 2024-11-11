package gold.service.impl;

import gold.mapper.GoldPriceMapper;
import gold.service.GoldPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GoldPriceServiceImpl implements GoldPriceService {

    @Autowired
    private GoldPriceMapper goldPriceMapper;

    @Override
    public BigDecimal newestPrice() {
        return goldPriceMapper.getNewestPrice();
    }
}
