package com.myntrademo.constant;

public final class MessageConstants {

    public static final String REGISTRATION_SUCCESS = "Registration successful. Please login.";
    public static final String LOGIN_SUCCESS = "Login successful.";
    public static final String LOGOUT_SUCCESS = "Logout successful.";

    public static final String REQUIRED_FULL_NAME = "Full name is required.";
    public static final String REQUIRED_EMAIL = "Email is required.";
    public static final String REQUIRED_PASSWORD = "Password is required.";
    public static final String REQUIRED_CONFIRM_PASSWORD = "Confirm password is required.";

    public static final String INVALID_FULL_NAME = "Full name must contain only valid characters.";
    public static final String INVALID_EMAIL = "Please enter a valid email address.";
    public static final String INVALID_PHONE = "Please enter a valid phone number.";
    public static final String INVALID_PASSWORD = "Password must contain at least 8 characters.";
    public static final String PASSWORD_MISMATCH = "Password and confirm password do not match.";

    public static final String EMAIL_ALREADY_EXISTS = "This email is already registered.";
    public static final String PHONE_ALREADY_EXISTS = "This phone number is already registered.";
    public static final String INVALID_LOGIN = "Invalid email or password.";
    public static final String INACTIVE_ACCOUNT = "Your account is inactive. Please contact support.";

    public static final String AUTH_REQUIRED = "Please login to continue.";
    public static final String ALREADY_LOGGED_IN = "You are already logged in.";
    public static final String ADMIN_ACCESS_DENIED = "You do not have permission to access the admin area.";

    public static final String CUSTOMER_ROLE_NOT_FOUND = "Customer role is not configured in the database.";
    public static final String SERVER_ERROR = "Something went wrong. Please try again.";

    public static final String SELECT_PRODUCT_VARIANT = "Please select a size before adding this item to bag.";
    public static final String INVALID_CART_QUANTITY = "Please enter a valid quantity.";
    public static final String PRODUCT_NOT_AVAILABLE = "This product is not available right now.";
    public static final String PRODUCT_OUT_OF_STOCK = "Selected size is out of stock.";
    public static final String CART_ITEM_ADDED = "Item added to bag.";
    public static final String CART_ITEM_UPDATED = "Bag item updated.";
    public static final String CART_ITEM_REMOVED = "Item removed from bag.";
    public static final String CART_ITEM_NOT_FOUND = "Bag item not found.";

    public static final String WISHLIST_ITEM_ADDED = "Item added to wishlist.";
public static final String WISHLIST_ITEM_REMOVED = "Item removed from wishlist.";
public static final String WISHLIST_ITEM_NOT_FOUND = "Wishlist item not found.";
public static final String WISHLIST_ITEM_ALREADY_EXISTS = "Item is already in wishlist.";
public static final String WISHLIST_ITEM_MOVED_TO_CART = "Item moved to bag.";
public static final String CART_ITEM_MOVED_TO_WISHLIST = "Item moved to wishlist.";

public static final String ADDRESS_ADDED = "Address added successfully.";
public static final String ADDRESS_NOT_FOUND = "Address not found.";
public static final String ADDRESS_REQUIRED = "Address is required.";
public static final String ADDRESS_DETAILS_REQUIRED = "Address details are required.";
public static final String REQUIRED_PHONE = "Phone number is required.";
public static final String REQUIRED_PINCODE = "Pincode is required.";
public static final String INVALID_PINCODE = "Please enter a valid 6 digit pincode.";
public static final String REQUIRED_ADDRESS_LINE = "Address line is required.";
public static final String REQUIRED_CITY = "City is required.";
public static final String REQUIRED_STATE = "State is required.";
public static final String ADDRESS_DELETED = "Address removed successfully.";
public static final String ADDRESS_DEFAULT_UPDATED = "Default address updated.";

public static final String COUPON_APPLIED = "Coupon applied successfully.";
public static final String COUPON_REMOVED = "Coupon removed.";
public static final String COUPON_NOT_FOUND = "Coupon not found.";
public static final String COUPON_NOT_APPLICABLE = "This coupon is not applicable for your bag.";

public static final String GIFT_PACKAGE_ADDED = "Gift package added to your bag.";
public static final String GIFT_PACKAGE_REMOVED = "Gift package removed from your bag.";
public static final String DONATION_ADDED = "Donation added successfully.";
public static final String DONATION_REMOVED = "Donation removed.";
public static final String INVALID_DONATION_AMOUNT = "Please select a valid donation amount.";

public static final String CHECKOUT_ADDRESS_SELECTED = "Delivery address selected.";
public static final String CHECKOUT_ADDRESS_REQUIRED = "Please select a delivery address.";
public static final String CART_EMPTY_FOR_CHECKOUT = "Your bag is empty. Add products before checkout.";

public static final String PAYMENT_METHOD_SELECTED = "Payment method selected.";
public static final String PAYMENT_METHOD_REQUIRED = "Please select a payment method.";

public static final String GIFT_CARD_APPLIED = "Gift card applied successfully.";
public static final String GIFT_CARD_REMOVED = "Gift card removed.";
public static final String GIFT_CARD_INVALID = "Please enter a valid gift card code.";

public static final String ORDER_PLACED_SUCCESSFULLY = "Order placed successfully.";
public static final String ORDER_PLACE_FAILED = "Unable to place order. Please try again.";
public static final String ORDER_NOT_FOUND = "Order not found.";

public static final String ORDER_CANCELLED_SUCCESSFULLY = "Order cancelled successfully.";
public static final String ORDER_CANCEL_FAILED = "Unable to cancel order.";
public static final String SUPPORT_QUERY_SENT = "Your support request has been submitted successfully.";
public static final String SUPPORT_QUERY_FAILED = "Unable to submit support request.";

    private MessageConstants() {
    }
}