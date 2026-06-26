package com.myntrademo.constant;

public final class RouteConstants {

    public static final String HOME = "/home";

    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";

    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_DETAIL = "/product";

    public static final String CART = "/cart";
    public static final String CART_ADD = "/cart/add";
    public static final String CART_UPDATE = "/cart/update";
    public static final String CART_REMOVE = "/cart/remove";
    public static final String CART_MOVE_TO_WISHLIST = "/cart/move-to-wishlist";

    public static final String WISHLIST = "/wishlist";
    public static final String WISHLIST_ADD = "/wishlist/add";
    public static final String WISHLIST_REMOVE = "/wishlist/remove";
    public static final String WISHLIST_MOVE_TO_CART = "/wishlist/move-to-cart";

    private RouteConstants() {
    }
}