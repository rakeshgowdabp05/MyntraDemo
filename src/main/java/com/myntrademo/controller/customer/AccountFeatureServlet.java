package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.dto.account.AccountFeaturePageDto;
import com.myntrademo.service.AccountFeatureService;
import com.myntrademo.service.impl.AccountFeatureServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {
        "/gift-cards",
        "/myntra-credit",
        "/coupons",
        "/saved-cards",
        "/saved-vpa"
})
public class AccountFeatureServlet extends HttpServlet {

    private static final String ACCOUNT_FEATURE_VIEW = "/WEB-INF/views/customer/account-feature.jsp";

    private final AccountFeatureService accountFeatureService = new AccountFeatureServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        try {
            AccountFeaturePageDto page = accountFeatureService.getPage(userId, request.getServletPath());

            request.setAttribute("accountFeaturePage", page);
            request.setAttribute("activeAccountMenu", page.getActiveMenu());

            request.getRequestDispatcher(ACCOUNT_FEATURE_VIEW).forward(request, response);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, "Unable to load account details. Please try again.");
            request.getRequestDispatcher(ACCOUNT_FEATURE_VIEW).forward(request, response);
        }
    }

    private Long getAuthenticatedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute(AttributeConstants.AUTH_USER_ID);

        if (userId instanceof Long value) {
            return value;
        }

        if (userId instanceof Integer value) {
            return value.longValue();
        }

        return null;
    }
}