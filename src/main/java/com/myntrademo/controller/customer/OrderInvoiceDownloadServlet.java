package com.myntrademo.controller.customer;

import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.dto.checkout.PlacedOrderItemDto;
import com.myntrademo.service.CheckoutOrderService;
import com.myntrademo.service.impl.CheckoutOrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/order/invoice/download")
public class OrderInvoiceDownloadServlet extends HttpServlet {

    private final CheckoutOrderService checkoutOrderService = new CheckoutOrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);
        Long orderId = parseLong(request.getParameter("id"));

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (orderId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            PlacedOrderDto order = checkoutOrderService.getOrderForUser(userId, orderId);

            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            byte[] pdf = buildInvoicePdf(order);

            response.setContentType("application/pdf");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"invoice-" + safeFileName(order.getDisplayOrderNumber()) + ".pdf\""
            );
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);

        } catch (SQLException exception) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] buildInvoicePdf(PlacedOrderDto order) throws IOException {
        List<String> lines = new ArrayList<>();

        lines.add("MyntraDemo Invoice");
        lines.add("Invoice Number: INV-" + order.getDisplayOrderNumber());
        lines.add("Order ID: " + order.getDisplayOrderNumber());
        lines.add("Invoice Date: " + order.getOrderedOnLabel());
        lines.add("Customer: " + order.getDisplayCustomerName());
        lines.add("Phone: " + order.getDisplayCustomerPhone());
        lines.add("Delivery Address: " + order.getDisplayDeliveryAddressText());
        lines.add(" ");
        lines.add("Items:");

        for (PlacedOrderItemDto item : order.getItems()) {
            lines.add(item.getQuantity() + " x " + item.getDisplayBrandName() + " - " + item.getDisplayProductName());
            lines.add("Amount: INR " + toAmount(item.getItemTotal()));
        }

        lines.add(" ");
        lines.add("Total MRP: INR " + toAmount(order.getTotalMrp()));
        lines.add("Discount: INR " + toAmount(order.getTotalDiscount()));

        if (order.getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
            lines.add("Coupon Discount: INR " + toAmount(order.getCouponDiscount()));
        }

        if (order.getGiftCardDiscount().compareTo(BigDecimal.ZERO) > 0) {
            lines.add("Gift Card Discount: INR " + toAmount(order.getGiftCardDiscount()));
        }

        if (order.isPaymentFeeAvailable()) {
            lines.add(order.getDisplayPaymentFeeLabel() + ": INR " + toAmount(order.getPaymentFee()));
        }

        lines.add("Total Paid: INR " + toAmount(order.getPayableAmount()));
        lines.add("Payment: " + order.getDisplayPaymentSentence());

        return createSimplePdf(lines);
    }

    private byte[] createSimplePdf(List<String> lines) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();

        write(output, "%PDF-1.4\n");

        offsets.add(output.size());
        write(output, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        offsets.add(output.size());
        write(output, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

        offsets.add(output.size());
        write(output, "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\nendobj\n");

        offsets.add(output.size());
        write(output, "4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

        StringBuilder content = new StringBuilder();
        content.append("BT\n/F1 12 Tf\n50 790 Td\n");

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);

            if (index == 0) {
                content.append("/F1 18 Tf\n");
            } else if (index == 1) {
                content.append("/F1 12 Tf\n");
            }

            content.append("(").append(escapePdf(line)).append(") Tj\n0 -20 Td\n");
        }

        content.append("ET\n");

        byte[] contentBytes = content.toString().getBytes(StandardCharsets.UTF_8);

        offsets.add(output.size());
        write(output, "5 0 obj\n<< /Length " + contentBytes.length + " >>\nstream\n");
        output.write(contentBytes);
        write(output, "\nendstream\nendobj\n");

        int xrefStart = output.size();

        write(output, "xref\n0 6\n");
        write(output, "0000000000 65535 f \n");

        for (Integer offset : offsets) {
            write(output, String.format("%010d 00000 n \n", offset));
        }

        write(output, "trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n");
        write(output, String.valueOf(xrefStart));
        write(output, "\n%%EOF");

        return output.toByteArray();
    }

    private void write(ByteArrayOutputStream output, String value) throws IOException {
        output.write(value.getBytes(StandardCharsets.UTF_8));
    }

    private String escapePdf(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }

    private String toAmount(BigDecimal value) {
        if (value == null) {
            return "0";
        }

        return value.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private String safeFileName(String value) {
        if (value == null || value.isBlank()) {
            return "order";
        }

        return value.replaceAll("[^a-zA-Z0-9_-]", "");
    }

    private Long getAuthenticatedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute("authUserId");

        if (userId instanceof Long value) {
            return value;
        }

        if (userId instanceof Integer value) {
            return value.longValue();
        }

        return null;
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}