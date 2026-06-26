package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.model.User;
import com.myntrademo.service.ProfileService;
import com.myntrademo.service.impl.ProfileServiceImpl;
import com.myntrademo.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/profile/update")
public class UpdateProfileServlet extends HttpServlet {

    private final ProfileService profileService = new ProfileServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        try {
            User profile = buildProfile(request);
            User updatedUser = profileService.updateProfile(userId, profile);

            HttpSession session = request.getSession();
            session.setAttribute(AttributeConstants.AUTH_USER_NAME, updatedUser.getFullName());
            session.setAttribute(AttributeConstants.SUCCESS_MESSAGE, "Profile updated successfully.");

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private User buildProfile(HttpServletRequest request) {
        User user = new User();

        user.setFullName(request.getParameter("fullName"));
        user.setPhone(request.getParameter("phone"));
        user.setGender(request.getParameter("gender"));
        user.setDateOfBirth(parseDate(request.getParameter("dateOfBirth")));

        return user;
    }

    private LocalDate parseDate(String value) {
        try {
            if (ValidationUtil.isBlank(value)) {
                return null;
            }

            return LocalDate.parse(value.trim());

        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Please enter a valid date of birth.");
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