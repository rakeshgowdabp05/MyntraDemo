package com.myntrademo.filter;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.RoleConstants;
import com.myntrademo.constant.RouteConstants;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization required.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(AttributeConstants.AUTH_USER_ID) == null) {
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.AUTH_REQUIRED);

            response.sendRedirect(request.getContextPath() + RouteConstants.LOGIN);
            return;
        }

        String roleName = (String) session.getAttribute(AttributeConstants.AUTH_USER_ROLE);

        if (!RoleConstants.ADMIN.equals(roleName)) {
            session.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ADMIN_ACCESS_DENIED);
            response.sendRedirect(request.getContextPath() + RouteConstants.HOME);
            return;
        }

        addNoCacheHeaders(response);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup required.
    }

    private void addNoCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}