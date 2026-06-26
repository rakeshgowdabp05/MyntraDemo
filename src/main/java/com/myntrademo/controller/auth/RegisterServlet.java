package com.myntrademo.controller.auth;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.RequestParamConstants;
import com.myntrademo.constant.RouteConstants;
import com.myntrademo.constant.ViewConstants;
import com.myntrademo.dto.RegisterRequest;
import com.myntrademo.service.AuthService;
import com.myntrademo.service.impl.AuthServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(RouteConstants.REGISTER)
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(ViewConstants.REGISTER_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RegisterRequest registerRequest = buildRegisterRequest(request);

        try {
            authService.registerCustomer(registerRequest);

            request.getSession().setAttribute(
                    AttributeConstants.SUCCESS_MESSAGE,
                    MessageConstants.REGISTRATION_SUCCESS
            );

            response.sendRedirect(request.getContextPath() + RouteConstants.LOGIN);

        } catch (IllegalArgumentException | IllegalStateException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
            keepFormValues(request, registerRequest);
            request.getRequestDispatcher(ViewConstants.REGISTER_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            keepFormValues(request, registerRequest);
            request.getRequestDispatcher(ViewConstants.REGISTER_VIEW).forward(request, response);
        }
    }

    private RegisterRequest buildRegisterRequest(HttpServletRequest request) {
        return new RegisterRequest(
                request.getParameter(RequestParamConstants.FULL_NAME),
                request.getParameter(RequestParamConstants.EMAIL),
                request.getParameter(RequestParamConstants.PHONE),
                request.getParameter(RequestParamConstants.PASSWORD),
                request.getParameter(RequestParamConstants.CONFIRM_PASSWORD)
        );
    }

    private void keepFormValues(HttpServletRequest request, RegisterRequest registerRequest) {
        request.setAttribute(RequestParamConstants.FULL_NAME, registerRequest.getFullName());
        request.setAttribute(RequestParamConstants.EMAIL, registerRequest.getEmail());
        request.setAttribute(RequestParamConstants.PHONE, registerRequest.getPhone());
    }
}