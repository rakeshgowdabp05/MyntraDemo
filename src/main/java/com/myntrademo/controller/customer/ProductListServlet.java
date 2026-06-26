package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.RouteConstants;
import com.myntrademo.constant.ViewConstants;
import com.myntrademo.dto.catalog.CategoryDto;
import com.myntrademo.dto.catalog.ProductFilterRequest;
import com.myntrademo.service.ProductCatalogService;
import com.myntrademo.service.impl.ProductCatalogServiceImpl;
import com.myntrademo.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@WebServlet(RouteConstants.PRODUCTS)
public class ProductListServlet extends HttpServlet {

    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_CATEGORY_ID = "categoryId";
    private static final String PARAM_BRAND_ID = "brandId";
    private static final String PARAM_SORT_BY = "sortBy";
    private static final String PARAM_SORT = "sort";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SECTION = "section";

    private static final String PAGE_TITLE = "pageTitle";
    private static final String ACTIVE_SECTION = "activeSection";

    private static final String SECTION_MEN = "men";
    private static final String SECTION_WOMEN = "women";
    private static final String SECTION_KIDS = "kids";
    private static final String SECTION_HOME = "home";
    private static final String SECTION_BEAUTY = "beauty";
    private static final String SECTION_GENZ = "genz";

    private final ProductCatalogService productCatalogService = new ProductCatalogServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<CategoryDto> categories = productCatalogService.getActiveCategories();

            ProductFilterRequest filterRequest = buildFilterRequest(request, categories);

            request.setAttribute(AttributeConstants.PRODUCT_PAGE, productCatalogService.getProductListPage(filterRequest));
            request.setAttribute(AttributeConstants.CATEGORIES, categories);
            request.setAttribute(AttributeConstants.BRANDS, productCatalogService.getActiveBrands());
            request.setAttribute(AttributeConstants.FILTER, filterRequest);
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);
            request.setAttribute(PAGE_TITLE, buildPageTitle(request, filterRequest, categories));
            request.setAttribute(ACTIVE_SECTION, buildActiveSection(request, filterRequest, categories));

            request.getRequestDispatcher(ViewConstants.PRODUCTS_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, "Unable to load products. Please try again.");
            request.setAttribute(AttributeConstants.FILTER, new ProductFilterRequest());
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);
            request.setAttribute(PAGE_TITLE, "Products");
            request.getRequestDispatcher(ViewConstants.PRODUCTS_VIEW).forward(request, response);
        }
    }

    private ProductFilterRequest buildFilterRequest(HttpServletRequest request, List<CategoryDto> categories) {
        ProductFilterRequest filterRequest = new ProductFilterRequest();

        String section = normalize(request.getParameter(PARAM_SECTION));
        String search = ValidationUtil.clean(request.getParameter(PARAM_SEARCH));
        Long categoryId = parseLong(request.getParameter(PARAM_CATEGORY_ID));

        if (categoryId == null && !ValidationUtil.isBlank(section)) {
            categoryId = resolveCategoryIdFromSection(section, categories);
        }

        if (categoryId == null && !ValidationUtil.isBlank(search)) {
            SearchCategoryMatch match = resolveCategoryFromSearch(search, categories);

            if (match.categoryId() != null) {
                categoryId = match.categoryId();
                search = match.remainingSearch();
                section = match.section();
            }
        }

        String sortBy = ValidationUtil.clean(request.getParameter(PARAM_SORT_BY));

        if (ValidationUtil.isBlank(sortBy)) {
            sortBy = ValidationUtil.clean(request.getParameter(PARAM_SORT));
        }

        if (SECTION_GENZ.equals(section) && ValidationUtil.isBlank(sortBy)) {
            sortBy = CatalogConstants.SORT_NEWEST;
        }

        filterRequest.setSearch(search);
        filterRequest.setCategoryId(categoryId);
        filterRequest.setBrandId(parseLong(request.getParameter(PARAM_BRAND_ID)));
        filterRequest.setSortBy(sortBy);
        filterRequest.setPage(parseIntOrDefault(request.getParameter(PARAM_PAGE), CatalogConstants.DEFAULT_PAGE));

        return filterRequest;
    }

    private Long resolveCategoryIdFromSection(String section, List<CategoryDto> categories) {
        String categoryName = switch (section) {
            case SECTION_MEN -> "men";
            case SECTION_WOMEN -> "women";
            case SECTION_KIDS -> "kids";
            case SECTION_HOME -> "home";
            case SECTION_BEAUTY -> "beauty";
            default -> null;
        };

        if (categoryName == null) {
            return null;
        }

        for (CategoryDto category : categories) {
            if (categoryName.equals(normalize(category.getCategoryName()))) {
                return category.getCategoryId();
            }
        }

        return null;
    }

    private SearchCategoryMatch resolveCategoryFromSearch(String search, List<CategoryDto> categories) {
        String cleanSearch = ValidationUtil.clean(search);

        if (ValidationUtil.isBlank(cleanSearch)) {
            return new SearchCategoryMatch(null, cleanSearch, null);
        }

        String lowerSearch = cleanSearch.toLowerCase(Locale.ROOT).trim();

        for (CategoryDto category : categories) {
            String categoryName = normalize(category.getCategoryName());

            if (lowerSearch.equals(categoryName)) {
                return new SearchCategoryMatch(category.getCategoryId(), null, categoryName);
            }

            if (lowerSearch.startsWith(categoryName + " ")) {
                String remaining = cleanSearch.substring(category.getCategoryName().length()).trim();
                return new SearchCategoryMatch(
                        category.getCategoryId(),
                        normalizeCommonSearchWord(remaining),
                        categoryName
                );
            }
        }

        if (lowerSearch.equals("mens")) {
            return new SearchCategoryMatch(resolveCategoryIdFromSection(SECTION_MEN, categories), null, SECTION_MEN);
        }

        if (lowerSearch.startsWith("mens ")) {
            return new SearchCategoryMatch(
                    resolveCategoryIdFromSection(SECTION_MEN, categories),
                    normalizeCommonSearchWord(cleanSearch.substring(5).trim()),
                    SECTION_MEN
            );
        }

        return new SearchCategoryMatch(null, cleanSearch, null);
    }

    private String normalizeCommonSearchWord(String value) {
        String cleanValue = ValidationUtil.clean(value);

        if (ValidationUtil.isBlank(cleanValue)) {
            return null;
        }

        String lower = cleanValue.toLowerCase(Locale.ROOT);

        return switch (lower) {
            case "shirts" -> "shirt";
            case "dresses" -> "dress";
            case "jackets" -> "jacket";
            case "wallets" -> "wallet";
            case "sarees" -> "saree";
            case "kurtas" -> "kurta";
            case "tshirts", "t-shirts", "tee shirts" -> "t-shirt";
            default -> cleanValue;
        };
    }

    private String buildPageTitle(HttpServletRequest request, ProductFilterRequest filterRequest, List<CategoryDto> categories) {
        String section = normalize(request.getParameter(PARAM_SECTION));

        if (SECTION_GENZ.equals(section)) {
            return "Latest Drops";
        }

        String categoryName = findCategoryName(filterRequest.getCategoryId(), categories);

        if (!ValidationUtil.isBlank(categoryName) && !ValidationUtil.isBlank(filterRequest.getSearch())) {
            return categoryName + " results for “" + filterRequest.getSearch() + "”";
        }

        if (!ValidationUtil.isBlank(categoryName)) {
            return categoryName;
        }

        if (!ValidationUtil.isBlank(filterRequest.getSearch())) {
            return "Search results for “" + filterRequest.getSearch() + "”";
        }

        return "All Products";
    }

    private String buildActiveSection(HttpServletRequest request, ProductFilterRequest filterRequest, List<CategoryDto> categories) {
        String section = normalize(request.getParameter(PARAM_SECTION));

        if (!ValidationUtil.isBlank(section)) {
            return section;
        }

        String categoryName = normalize(findCategoryName(filterRequest.getCategoryId(), categories));

        if (SECTION_MEN.equals(categoryName)
                || SECTION_WOMEN.equals(categoryName)
                || SECTION_KIDS.equals(categoryName)
                || SECTION_HOME.equals(categoryName)
                || SECTION_BEAUTY.equals(categoryName)) {
            return categoryName;
        }

        return "";
    }

    private String findCategoryName(Long categoryId, List<CategoryDto> categories) {
        if (categoryId == null) {
            return null;
        }

        for (CategoryDto category : categories) {
            if (categoryId.equals(category.getCategoryId())) {
                return category.getCategoryName();
            }
        }

        return null;
    }

    private String normalize(String value) {
        if (ValidationUtil.isBlank(value)) {
            return "";
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }

    private Long parseLong(String value) {
        try {
            if (ValidationUtil.isBlank(value)) {
                return null;
            }

            return Long.parseLong(value);

        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            if (ValidationUtil.isBlank(value)) {
                return defaultValue;
            }

            return Integer.parseInt(value);

        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private record SearchCategoryMatch(Long categoryId, String remainingSearch, String section) {
    }
}