package com.myntrademo.dto.checkout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomerOrderCardDto {

    private Long orderId;
    private String orderNumber;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethodCode;
    private int totalItems;
    private BigDecimal payableAmount = BigDecimal.ZERO;
    private LocalDate estimatedDeliveryDate;
    private LocalDateTime createdAt;

    private Long productId;
    private String brandName;
    private String productName;
    private String sizeLabel;
    private String imageUrl;
    private int quantity;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getDisplayOrderNumber() {
        return orderNumber == null ? "" : orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getDisplayOrderStatus() {
        return orderStatus == null ? "" : orderStatus.replace("_", " ");
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(orderStatus);
    }

    public boolean isPlaced() {
        return "PLACED".equalsIgnoreCase(orderStatus);
    }

    public boolean isDelivered() {
        return "DELIVERED".equalsIgnoreCase(orderStatus);
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getDisplayPaymentStatus() {
        return paymentStatus == null ? "" : paymentStatus.replace("_", " ");
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public String getDisplayPaymentMethod() {
        if (paymentMethodCode == null || paymentMethodCode.isBlank()) {
            return "";
        }

        return paymentMethodCode.replace("_", " ");
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public String getTotalItemsLabel() {
        if (totalItems == 1) {
            return "1 item";
        }

        return totalItems + " items";
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount == null ? BigDecimal.ZERO : payableAmount;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public String getEstimatedDeliveryLabel() {
        if (isCancelled()) {
            return "Cancelled";
        }

        LocalDate date = estimatedDeliveryDate == null ? LocalDate.now().plusDays(3) : estimatedDeliveryDate;
        return date.format(DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.ENGLISH));
    }

    public String getStatusLine() {
        if (isCancelled()) {
            return "Cancelled on " + getOrderedOnLabel();
        }

        if (isDelivered()) {
            return "Delivered";
        }

        return "Arriving by " + getEstimatedDeliveryLabel();
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getOrderedOnLabel() {
        LocalDateTime date = createdAt == null ? LocalDateTime.now() : createdAt;
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
    }

    public String getPlacedDateLabel() {
        LocalDateTime date = createdAt == null ? LocalDateTime.now() : createdAt;
        return date.format(DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH));
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getProductId() {
        return productId;
    }

    public boolean isProductLinkAvailable() {
        return productId != null;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getDisplayBrandName() {
        return brandName == null || brandName.isBlank() ? "MyntraDemo" : brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public String getDisplayProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }

    public String getDisplaySizeLabel() {
        return sizeLabel == null || sizeLabel.isBlank() ? "Free Size" : sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isImageAvailable() {
        return imageUrl != null && !imageUrl.isBlank();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getQuantityLabel() {
        return "Quantity: " + quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}