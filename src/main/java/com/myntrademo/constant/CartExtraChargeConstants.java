package com.myntrademo.constant;

import java.math.BigDecimal;
import java.util.List;

public final class CartExtraChargeConstants {

    public static final BigDecimal GIFT_PACKAGE_FEE = BigDecimal.valueOf(35);

    public static final List<BigDecimal> DONATION_OPTIONS = List.of(
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(20),
            BigDecimal.valueOf(50),
            BigDecimal.valueOf(100)
    );

    private CartExtraChargeConstants() {
    }
}