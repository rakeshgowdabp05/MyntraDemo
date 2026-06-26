package com.myntrademo.service.impl;

import com.myntrademo.dao.GiftCardDao;
import com.myntrademo.dao.impl.JdbcGiftCardDao;
import com.myntrademo.dto.checkout.GiftCardDto;
import com.myntrademo.service.GiftCardService;

import java.math.BigDecimal;
import java.sql.SQLException;

public class GiftCardServiceImpl implements GiftCardService {

    private final GiftCardDao giftCardDao = new JdbcGiftCardDao();

    @Override
    public GiftCardDto findUsableGiftCard(String giftCardCode, BigDecimal orderAmount) throws SQLException {
        if (giftCardCode == null || giftCardCode.isBlank() || orderAmount == null) {
            return null;
        }

        GiftCardDto giftCard = giftCardDao.findActiveGiftCard(giftCardCode.trim().toUpperCase());

        if (giftCard == null) {
            return null;
        }

        if (orderAmount.compareTo(giftCard.getMinimumOrderAmount()) < 0) {
            return null;
        }

        return giftCard;
    }

    @Override
    public BigDecimal calculateDiscount(GiftCardDto giftCard, BigDecimal orderAmount) {
        if (giftCard == null || orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if (giftCard.getBalanceAmount().compareTo(orderAmount) > 0) {
            return orderAmount;
        }

        return giftCard.getBalanceAmount();
    }
}