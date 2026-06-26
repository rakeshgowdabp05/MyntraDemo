package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.RouteConstants;
import com.myntrademo.constant.ViewConstants;
import com.myntrademo.dto.catalog.ProductDetailDto;
import com.myntrademo.service.ProductCatalogService;
import com.myntrademo.service.impl.ProductCatalogServiceImpl;
import com.myntrademo.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(RouteConstants.PRODUCT_DETAIL)
public class ProductDetailServlet extends HttpServlet {

    private final ProductCatalogService productCatalogService = new ProductCatalogServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long productId = parseProductId(request.getParameter("id"));

        if (productId == null) {
            response.sendRedirect(request.getContextPath() + RouteConstants.PRODUCTS);
            return;
        }

        try {
            Optional<ProductDetailDto> productDetail = productCatalogService.getProductDetail(
                    productId,
                    getAuthenticatedUserId(request)
            );

            if (productDetail.isEmpty()) {
                response.sendRedirect(request.getContextPath() + RouteConstants.PRODUCTS);
                return;
            }

            request.setAttribute(AttributeConstants.PRODUCT_DETAIL, productDetail.get());
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);
            request.getRequestDispatcher(ViewConstants.PRODUCT_DETAIL_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, "Unable to load product details. Please try again.");
            request.getRequestDispatcher(ViewConstants.PRODUCT_DETAIL_VIEW).forward(request, response);
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

    private Long parseProductId(String value) {
        try {
            if (ValidationUtil.isBlank(value)) {
                return null;
            }

            return Long.parseLong(value);

        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
