package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.ViewConstants;
import com.myntrademo.service.WishlistService;
import com.myntrademo.service.impl.WishlistServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/wishlist")
public class WishlistPageServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        try {
            request.setAttribute(AttributeConstants.WISHLIST_PAGE, wishlistService.getWishlistPage(userId));
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);
            request.getRequestDispatcher(ViewConstants.WISHLIST_VIEW).forward(request, response);

        } catch (IllegalArgumentException exception) {
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, "Unable to load wishlist. Please try again.");
            request.getRequestDispatcher(ViewConstants.WISHLIST_VIEW).forward(request, response);
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