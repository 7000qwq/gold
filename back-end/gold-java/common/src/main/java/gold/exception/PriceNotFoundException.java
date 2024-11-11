package gold.exception;

/**
 * 实时金价获取异常
 */
public class PriceNotFoundException extends BaseException {

    public PriceNotFoundException() {
    }

    public PriceNotFoundException(String msg) {
        super(msg);
    }

}
