package com.myntrademo.dao.impl;

import com.myntrademo.dao.GiftCardDao;
import com.myntrademo.dto.checkout.GiftCardDto;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcGiftCardDao implements GiftCardDao {

    @Override
    public GiftCardDto findActiveGiftCard(String giftCardCode) throws SQLException {
        String sql = """
                SELECT
                    gift_card_code,
                    gift_card_title,
                    balance_amount,
                    minimum_order_amount,
                    expires_on
                FROM gift_cards
                WHERE gift_card_code = ?
                  AND active = TRUE
                  AND (expires_on IS NULL OR expires_on >= CURRENT_DATE)
                LIMIT 1
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, giftCardCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    GiftCardDto giftCard = new GiftCardDto();
                    giftCard.setGiftCardCode(resultSet.getString("gift_card_code"));
                    giftCard.setGiftCardTitle(resultSet.getString("gift_card_title"));
                    giftCard.setBalanceAmount(resultSet.getBigDecimal("balance_amount"));
                    giftCard.setMinimumOrderAmount(resultSet.getBigDecimal("minimum_order_amount"));

                    Date expiresOn = resultSet.getDate("expires_on");
                    if (expiresOn != null) {
                        giftCard.setExpiresOn(expiresOn.toLocalDate());
                    }

                    return giftCard;
                }
            }
        }

        return null;
    }
}