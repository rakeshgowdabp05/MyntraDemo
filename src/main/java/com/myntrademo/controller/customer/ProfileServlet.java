package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.model.User;
import com.myntrademo.service.ProfileService;
import com.myntrademo.service.impl.ProfileServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final String PROFILE_VIEW = "/WEB-INF/views/customer/profile.jsp";

    private final ProfileService profileService = new ProfileServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessagesToRequest(request);

        Long userId = getAuthenticatedUserId(request);

        try {
            User profile = profileService.getProfile(userId);
            request.setAttribute("profileUser", profile);
            request.setAttribute("activeAccountMenu", "profile");
            request.getRequestDispatcher(PROFILE_VIEW).forward(request, response);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            request.getRequestDispatcher(PROFILE_VIEW).forward(request, response);
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