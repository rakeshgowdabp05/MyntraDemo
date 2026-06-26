package com.myntrademo.service.impl;

import com.myntrademo.dao.AccountFeatureDao;
import com.myntrademo.dao.impl.JdbcAccountFeatureDao;
import com.myntrademo.dto.account.AccountFeaturePageDto;
import com.myntrademo.service.AccountFeatureService;

import java.sql.SQLException;

public class AccountFeatureServiceImpl implements AccountFeatureService {

    private final AccountFeatureDao accountFeatureDao;

    public AccountFeatureServiceImpl() {
        this.accountFeatureDao = new JdbcAccountFeatureDao();
    }

    @Override
    public AccountFeaturePageDto getPage(Long userId, String path) throws SQLException {
        if (userId == null) {
            throw new IllegalArgumentException("Please login to continue.");
        }

        AccountFeaturePageDto page = new AccountFeaturePageDto();

        switch (path) {
            case "/gift-cards" -> {
                page.setTitle("Gift Cards");
                page.setSubtitle("View and manage your gift card balance.");
                page.setEmptyTitle("No gift cards available");
                page.setEmptyDescription("Gift cards added to your account will appear here.");
                page.setIconText("🎁");
                page.setActiveMenu("giftCards");
                page.setItems(accountFeatureDao.findGiftCards(userId));
            }

            case "/myntra-credit" -> {
                page.setTitle("Myntra Credit");
                page.setSubtitle("Track your store credit and refunds.");
                page.setEmptyTitle("No credit balance available");
                page.setEmptyDescription("Refunds or promotional credit will appear here when available.");
                page.setIconText("₹");
                page.setActiveMenu("credit");
                page.setItems(accountFeatureDao.findCreditTransactions(userId));
            }

            case "/coupons" -> {
                page.setTitle("Coupons");
                page.setSubtitle("Apply available coupons during checkout.");
                page.setEmptyTitle("No coupons available");
                page.setEmptyDescription("Coupons linked to your account will appear here.");
                page.setIconText("%");
                page.setActiveMenu("coupons");
                page.setItems(accountFeatureDao.findCoupons(userId));
            }

            case "/saved-cards" -> {
                page.setTitle("Saved Cards");
                page.setSubtitle("Manage cards saved for faster checkout.");
                page.setEmptyTitle("No saved cards");
                page.setEmptyDescription("Saved payment cards will appear here.");
                page.setIconText("💳");
                page.setActiveMenu("savedCards");
                page.setItems(accountFeatureDao.findSavedCards(userId));
            }

            case "/saved-vpa" -> {
                page.setTitle("Saved VPA");
                page.setSubtitle("Manage saved UPI IDs for faster payments.");
                page.setEmptyTitle("No saved VPA");
                page.setEmptyDescription("Saved UPI IDs will appear here after payment setup.");
                page.setIconText("UPI");
                page.setActiveMenu("savedVpa");
                page.setItems(accountFeatureDao.findSavedVpa(userId));
            }

            default -> throw new IllegalArgumentException("Page not found.");
        }

        return page;
    }
}