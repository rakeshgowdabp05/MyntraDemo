package com.myntrademo.controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/studio")
public class StudioServlet extends HttpServlet {

    private static final String STUDIO_VIEW = "/WEB-INF/views/customer/studio.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(STUDIO_VIEW).forward(request, response);
    }
}