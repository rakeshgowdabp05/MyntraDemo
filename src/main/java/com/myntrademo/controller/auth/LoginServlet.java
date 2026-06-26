package com.myntrademo.controller.auth;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.RequestParamConstants;
import com.myntrademo.constant.RouteConstants;
import com.myntrademo.constant.ViewConstants;
import com.myntrademo.dto.AuthenticatedUser;
import com.myntrademo.dto.LoginRequest;
import com.myntrademo.service.AuthService;
import com.myntrademo.service.impl.AuthServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(RouteConstants.LOGIN)
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        moveFlashMessagesToRequest(request);
        request.getRequestDispatcher(ViewConstants.LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LoginRequest loginRequest = buildLoginRequest(request);

        try {
            AuthenticatedUser authenticatedUser = authService.login(loginRequest);
            createAuthenticatedSession(request, authenticatedUser);

            response.sendRedirect(request.getContextPath() + RouteConstants.HOME);

        } catch (IllegalArgumentException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
            request.setAttribute(RequestParamConstants.EMAIL, loginRequest.getEmail());
            request.getRequestDispatcher(ViewConstants.LOGIN_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            request.setAttribute(RequestParamConstants.EMAIL, loginRequest.getEmail());
            request.getRequestDispatcher(ViewConstants.LOGIN_VIEW).forward(request, response);
        }
    }

    private LoginRequest buildLoginRequest(HttpServletRequest request) {
        return new LoginRequest(
                request.getParameter(RequestParamConstants.EMAIL),
                request.getParameter(RequestParamConstants.PASSWORD)
        );
    }

    private void createAuthenticatedSession(HttpServletRequest request, AuthenticatedUser authenticatedUser) {
        HttpSession oldSession = request.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(AttributeConstants.AUTH_USER_ID, authenticatedUser.getUserId());
        session.setAttribute(AttributeConstants.AUTH_USER_NAME, authenticatedUser.getFullName());
        session.setAttribute(AttributeConstants.AUTH_USER_EMAIL, authenticatedUser.getEmail());
        session.setAttribute(AttributeConstants.AUTH_USER_ROLE, authenticatedUser.getRoleName());
    }

    private void moveFlashMessagesToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        moveSessionAttributeToRequest(session, request, AttributeConstants.SUCCESS_MESSAGE);
        moveSessionAttributeToRequest(session, request, AttributeConstants.ERROR_MESSAGE);
    }

    private void moveSessionAttributeToRequest(
            HttpSession session,
            HttpServletRequest request,
            String attributeName
    ) {
        Object value = session.getAttribute(attributeName);

        if (value != null) {
            request.setAttribute(attributeName, value);
            session.removeAttribute(attributeName);
        }
    }
}