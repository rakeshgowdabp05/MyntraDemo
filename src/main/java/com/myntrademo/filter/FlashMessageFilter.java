package com.myntrademo.filter;

import com.myntrademo.constant.AttributeConstants;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class FlashMessageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization required.
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest request) {
            moveFlashMessage(request, AttributeConstants.SUCCESS_MESSAGE);
            moveFlashMessage(request, AttributeConstants.ERROR_MESSAGE);
            moveFlashMessage(request, AttributeConstants.DATABASE_ERROR);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // No cleanup required.
    }

    private void moveFlashMessage(HttpServletRequest request, String attributeName) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        Object message = session.getAttribute(attributeName);

        if (message != null) {
            request.setAttribute(attributeName, message);
            session.removeAttribute(attributeName);
        }
    }
}