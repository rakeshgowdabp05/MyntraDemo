package com.myntrademo.service;

import com.myntrademo.dto.checkout.GiftCardDto;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface GiftCardService {

    GiftCardDto findUsableGiftCard(String giftCardCode, BigDecimal orderAmount) throws SQLException;

    BigDecimal calculateDiscount(GiftCardDto giftCard, BigDecimal orderAmount);
}