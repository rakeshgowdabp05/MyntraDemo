package com.myntrademo.constant;

public final class CatalogConstants {

    public static final int DEFAULT_PAGE = 1;

    /*
     * Myntra-style product listing:
     * 5 products per row x 5 rows = 25 products per page.
     */
    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final String DEFAULT_SORT = "newest";
    public static final String SORT_NEWEST = "newest";
    public static final String SORT_PRICE_LOW_HIGH = "price_low_high";
    public static final String SORT_PRICE_HIGH_LOW = "price_high_low";
    public static final String SORT_NAME_ASC = "name_asc";

    public static final String ACTIVE_PRODUCT_STATUS = "ACTIVE";

    public static final String DEFAULT_CURRENCY_SYMBOL = "₹";

    private CatalogConstants() {
    }
}