package com.myntrademo.dao;

import com.myntrademo.dto.checkout.GiftCardDto;

import java.sql.SQLException;

public interface GiftCardDao {

    GiftCardDto findActiveGiftCard(String giftCardCode) throws SQLException;
}