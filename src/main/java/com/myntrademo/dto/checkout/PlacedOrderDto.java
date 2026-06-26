package com.myntrademo.dto.checkout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlacedOrderDto {

    private Long orderId;
    private String orderNumber;
    private Long userId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddressText;
    private String deliveryPincode;

    private int totalItems;
    private BigDecimal totalMrp = BigDecimal.ZERO;
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    private BigDecimal couponDiscount = BigDecimal.ZERO;
    private BigDecimal giftPackageFee = BigDecimal.ZERO;
    private BigDecimal donationAmount = BigDecimal.ZERO;
    private BigDecimal giftCardDiscount = BigDecimal.ZERO;
    private BigDecimal paymentFee = BigDecimal.ZERO;
    private BigDecimal payableAmount = BigDecimal.ZERO;

    private String orderStatus;
    private String paymentStatus;
    private String paymentMethodCode;
    private String paymentOptionCode;

    private LocalDate estimatedDeliveryDate;
    private LocalDateTime createdAt;

    private List<PlacedOrderItemDto> items = new ArrayList<>();

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDisplayCustomerName() {
        return customerName == null || customerName.isBlank() ? "Customer" : customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getDisplayCustomerPhone() {
        return customerPhone == null ? "" : customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDeliveryAddressText() {
        return deliveryAddressText;
    }

    public String getDisplayDeliveryAddressText() {
        return deliveryAddressText == null ? "" : deliveryAddressText;
    }

    public void setDeliveryAddressText(String deliveryAddressText) {
        this.deliveryAddressText = deliveryAddressText;
    }

    public String getDeliveryPincode() {
        return deliveryPincode;
    }

    public void setDeliveryPincode(String deliveryPincode) {
        this.deliveryPincode = deliveryPincode;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalMrp() {
        return totalMrp;
    }

    public void setTotalMrp(BigDecimal totalMrp) {
        this.totalMrp = totalMrp == null ? BigDecimal.ZERO : totalMrp;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount == null ? BigDecimal.ZERO : totalDiscount;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount == null ? BigDecimal.ZERO : couponDiscount;
    }

    public BigDecimal getGiftPackageFee() {
        return giftPackageFee;
    }

    public void setGiftPackageFee(BigDecimal giftPackageFee) {
        this.giftPackageFee = giftPackageFee == null ? BigDecimal.ZERO : giftPackageFee;
    }

    public BigDecimal getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(BigDecimal donationAmount) {
        this.donationAmount = donationAmount == null ? BigDecimal.ZERO : donationAmount;
    }

    public BigDecimal getGiftCardDiscount() {
        return giftCardDiscount;
    }

    public void setGiftCardDiscount(BigDecimal giftCardDiscount) {
        this.giftCardDiscount = giftCardDiscount == null ? BigDecimal.ZERO : giftCardDiscount;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public boolean isPaymentFeeAvailable() {
        return paymentFee != null && paymentFee.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getDisplayPaymentFeeLabel() {
        if ("CASH_ON_DELIVERY".equals(paymentMethodCode)) {
            return "Cash/Pay on Delivery Fee";
        }

        return "Payment Fee";
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee == null ? BigDecimal.ZERO : paymentFee;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount == null ? BigDecimal.ZERO : payableAmount;
    }

    public BigDecimal getDiscountedPrice() {
        BigDecimal discounted = totalMrp
                .subtract(totalDiscount)
                .subtract(couponDiscount)
                .subtract(giftCardDiscount);

        if (discounted.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return discounted;
    }

    public BigDecimal getTotalSavedAmount() {
        BigDecimal saved = totalDiscount.add(couponDiscount).add(giftCardDiscount);

        if (saved.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return saved;
    }

    public boolean isSavingsAvailable() {
        return getTotalSavedAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getDisplayOrderStatus() {
        return orderStatus == null ? "" : orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getDisplayPaymentStatus() {
        return paymentStatus == null ? "" : paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public String getDisplayPaymentMethod() {
        if (paymentMethodCode == null) {
            return "";
        }

        return paymentMethodCode.replace("_", " ");
    }

    public String getDisplayPaymentSentence() {
        if ("CASH_ON_DELIVERY".equals(paymentMethodCode)) {
            return "Pay on delivery.";
        }

        return "Paid using " + getDisplayPaymentMethod() + ".";
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getPaymentOptionCode() {
        return paymentOptionCode;
    }

    public void setPaymentOptionCode(String paymentOptionCode) {
        this.paymentOptionCode = paymentOptionCode;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public String getEstimatedDeliveryLabel() {
        LocalDate date = estimatedDeliveryDate == null ? LocalDate.now().plusDays(3) : estimatedDeliveryDate;
        return date.format(DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.ENGLISH));
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

    public List<PlacedOrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<PlacedOrderItemDto> items) {
        this.items = items == null ? new ArrayList<>() : items;
    }

    public boolean isItemsAvailable() {
        return items != null && !items.isEmpty();
    }

    public PlacedOrderItemDto getPrimaryItem() {
        if (items == null || items.isEmpty()) {
            return null;
        }

        return items.get(0);
    }
}