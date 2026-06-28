# MyntraDemo — Project Documentation

> A complete end-to-end guide to understanding how MyntraDemo works — architecture, database, request flow, modules, and code design.

---

## Table of Contents

1. [What is MyntraDemo?](#1-what-is-myntrademo)
2. [Technology Choices — Why These Tools?](#2-technology-choices--why-these-tools)
3. [Architecture Overview](#3-architecture-overview)
4. [Project Package Structure](#4-project-package-structure)
5. [Database Design](#5-database-design)
6. [How a Request Flows Through the App](#6-how-a-request-flows-through-the-app)
7. [Security Layer](#7-security-layer)
8. [Module-by-Module Walkthrough](#8-module-by-module-walkthrough)
9. [Key Design Decisions](#9-key-design-decisions)
10. [Known Limitations & Future Improvements](#10-known-limitations--future-improvements)

---

## 1. What is MyntraDemo?

MyntraDemo is a full-stack e-commerce web application for fashion shopping, inspired by the Myntra platform. It allows a customer to:

- Browse products by category or search
- View detailed product pages with images, variants, specs, and reviews
- Manage a wishlist and shopping cart
- Apply coupons and gift cards
- Go through a multi-step checkout (address → payment → confirm)
- Place orders and view order history
- Download PDF invoices
- Manage their profile and saved addresses

The project is built using **Java Servlets, JSP, JDBC, and MySQL** — with no Spring Boot, no JPA/Hibernate, no React, and no CSS framework like Bootstrap or Tailwind. Every layer is written from scratch, which means the code clearly demonstrates understanding of how Java web applications work under the hood.

---

## 2. Technology Choices — Why These Tools?

| Technology | Role | Why |
|---|---|---|
| Java 21 | Backend language | Modern LTS version; supports text blocks for SQL |
| Jakarta Servlet 6.0 | HTTP handling | The foundation of Java web apps; used in real enterprise systems |
| JSP + JSTL | View rendering | Server-side templating; avoids mixing Java logic into HTML |
| JDBC | Database access | Raw SQL control; demonstrates understanding without ORM magic |
| MySQL | Relational database | Industry-standard; matches the domain (relational e-commerce data) |
| BCrypt | Password hashing | Industry standard; adaptive cost factor protects against brute force |
| Maven | Build tool | Dependency management and WAR packaging |
| Apache Tomcat 10.1 | Server | The standard Jakarta EE servlet container |
| Apache Commons FileUpload | File handling | For product/banner image uploads |

**No Spring Boot, no JPA, no Bootstrap.** This is intentional — the project exists to show that you understand the full Java web stack, not just how to use a framework that hides it.

---

## 3. Architecture Overview

MyntraDemo follows the **MVC (Model-View-Controller)** pattern extended with a **Service Layer** and **DAO (Data Access Object) Layer**.

```
┌─────────────────────────────────────────────────────┐
│                      BROWSER                        │
│         (HTML, CSS, JavaScript, JSP output)         │
└────────────────────────┬────────────────────────────┘
                         │  HTTP Request (GET / POST)
                         ▼
┌─────────────────────────────────────────────────────┐
│                  FILTER CHAIN                       │
│   FlashMessageFilter → AuthFilter / GuestFilter     │
│               → AdminFilter                         │
└────────────────────────┬────────────────────────────┘
                         │  Request passes through
                         ▼
┌─────────────────────────────────────────────────────┐
│            CONTROLLER (Servlet)                     │
│  - Reads request parameters                         │
│  - Reads session attributes                         │
│  - Calls one or more services                       │
│  - Sets request/session attributes                  │
│  - Forwards to JSP or sends redirect                │
└────────────────────────┬────────────────────────────┘
                         │  Method call
                         ▼
┌─────────────────────────────────────────────────────┐
│              SERVICE LAYER                          │
│  - Business logic and validation                    │
│  - Orchestrates multiple DAO calls                  │
│  - Throws meaningful exceptions                     │
│  - Returns DTOs to the controller                   │
└────────────────────────┬────────────────────────────┘
                         │  Method call
                         ▼
┌─────────────────────────────────────────────────────┐
│               DAO LAYER (JDBC)                      │
│  - All SQL queries using PreparedStatement           │
│  - Maps ResultSet rows to DTO objects               │
│  - Handles transactions (commit/rollback)           │
│  - Returns typed results to service                 │
└────────────────────────┬────────────────────────────┘
                         │  SQL
                         ▼
┌─────────────────────────────────────────────────────┐
│               MySQL DATABASE                        │
│           (26 tables, relational schema)            │
└─────────────────────────────────────────────────────┘
                         │
                         │  Data returned up the chain
                         ▼
┌─────────────────────────────────────────────────────┐
│               JSP VIEW (WEB-INF/views)              │
│  - JSTL for logic (c:if, c:forEach, c:out)          │
│  - EL expressions for rendering data                │
│  - Included fragments (header, toast)               │
└─────────────────────────────────────────────────────┘
```

### Key Design Rules

Every layer communicates through **interfaces**. For example, `CartService` is an interface and `CartServiceImpl` is the implementation. This means:
- You can swap implementations without touching the controller
- It is easier to discuss testability in interviews (mock the interface)

---

## 4. Project Package Structure

```
com.myntrademo/
│
├── constant/           ← No magic strings anywhere in the app
│   ├── RouteConstants          (/home, /cart, /login, ...)
│   ├── ViewConstants           (paths to JSP files)
│   ├── AttributeConstants      (session/request attribute keys)
│   ├── RequestParamConstants   (HTTP parameter names)
│   ├── MessageConstants        (all user-facing messages)
│   ├── SecurityConstants       (BCrypt cost, password length)
│   ├── CatalogConstants        (product status values)
│   ├── CartExtraChargeConstants (delivery charge, GST rates)
│   ├── CheckoutAttributeConstants
│   ├── CheckoutOrderStatusConstants
│   ├── CheckoutViewConstants
│   ├── PaymentMethodConstants
│   ├── AddressConstants
│   └── SupportConstants
│
├── controller/
│   ├── auth/
│   │   ├── LoginServlet
│   │   ├── RegisterServlet
│   │   └── LogoutServlet
│   └── customer/           ← ~35 servlets covering the full customer journey
│       ├── HomeServlet
│       ├── ProductListServlet
│       ├── ProductDetailServlet
│       ├── CartPageServlet, AddToCartServlet, UpdateCartItemServlet,
│       │   RemoveCartItemServlet, MoveCartToWishlistServlet
│       ├── WishlistPageServlet, AddToWishlistServlet,
│       │   RemoveWishlistItemServlet, MoveWishlistToCartServlet
│       ├── ApplyCouponServlet, RemoveCouponServlet
│       ├── ApplyGiftCardServlet, RemoveGiftCardServlet
│       ├── ToggleGiftPackageServlet
│       ├── UpdateDonationServlet, RemoveDonationServlet
│       ├── CheckoutAddressServlet, SelectCheckoutAddressServlet
│       ├── CheckoutPaymentServlet
│       ├── SelectPaymentMethodServlet, SelectPaymentOptionServlet
│       ├── PlaceOrderServlet
│       ├── OrderSuccessServlet
│       ├── OrdersServlet, OrderDetailsServlet
│       ├── CancelOrderServlet
│       ├── OrderInvoiceDownloadServlet
│       ├── UpdateOrderAddressServlet
│       ├── AddressPageServlet, AddAddressServlet, UpdateAddressServlet,
│       │   DeleteAddressServlet, SetDefaultAddressServlet
│       ├── ProfileServlet, UpdateProfileServlet
│       ├── AccountFeatureServlet
│       ├── HelpCenterServlet, HelpChatServlet
│       └── StudioServlet
│
├── dao/                    ← Interface definitions
│   └── impl/               ← JDBC implementations
│       ├── JdbcProductDao      (26KB — most complex, handles all catalog queries)
│       ├── JdbcCartDao         (handles cart + coupon + stock validation)
│       ├── JdbcWishlistDao     (wishlist + cart cross-reference)
│       ├── JdbcCheckoutOrderDao (order creation with transactions)
│       ├── JdbcUserDao
│       ├── JdbcAddressDao
│       ├── JdbcPaymentMethodDao
│       ├── JdbcCouponDao
│       ├── JdbcGiftCardDao
│       ├── JdbcAccountFeatureDao
│       ├── JdbcBrandDao
│       ├── JdbcCategoryDao
│       └── JdbcRoleDao
│
├── dto/                    ← Data Transfer Objects (no business logic, just data carriers)
│   ├── catalog/            (ProductCardDto, ProductDetailDto, ProductFilterRequest, ...)
│   ├── cart/               (CartPageDto, CartItemDto, CouponDto, ...)
│   ├── checkout/           (PlaceOrderRequest, PlacedOrderDto, PaymentMethodDto, ...)
│   ├── home/               (HomePageDto, HomeCategoryDto, HomeBrandDto, ...)
│   ├── wishlist/           (WishlistItemDto, WishlistPageDto)
│   ├── account/            (AccountFeatureItemDto, AccountFeaturePageDto)
│   ├── LoginRequest
│   ├── RegisterRequest
│   └── AuthenticatedUser
│
├── filter/
│   ├── AuthFilter          ← Redirects unauthenticated users to /login
│   ├── GuestFilter         ← Redirects logged-in users away from /login and /register
│   ├── AdminFilter         ← Protects all /admin/* routes
│   └── FlashMessageFilter  ← Moves flash messages from session to request
│
├── model/
│   └── User                ← Domain model for the user entity
│
├── service/                ← Interface definitions
│   └── impl/               ← Business logic implementations
│
└── util/
    ├── DBConnection        ← Opens MySQL connection from environment variables
    ├── PasswordUtil        ← BCrypt hash and verify
    └── ValidationUtil      ← Email, phone, name, password validation with regex
```

---

## 5. Database Design

The schema has **26 tables**. Here is how they relate:

### Core Tables

**`users`** — Stores customer and admin accounts. Password is stored as a BCrypt hash, never in plaintext. Each user has a role (foreign key to `roles`).

**`roles`** — Simple lookup: `CUSTOMER`, `ADMIN`.

**`addresses`** — Each user can have multiple saved addresses. One is flagged as `is_default`. Stores full address details including pincode and landmark.

### Catalog Tables

**`products`** — Master product record. Has `base_price`, `selling_price`, `product_status` (ACTIVE/INACTIVE), slug for URL-friendly access.

**`categories`** / **`brands`** — Lookup tables. Products belong to one category and one brand.

**`product_images`** — Multiple images per product. One is marked `is_primary`. Ordered by `sort_order`.

**`product_variants`** — Each product can have size/colour variants with individual stock counts (SKU-level).

**`product_specifications`** — Key-value pairs for product specs (e.g. Material: Cotton, Fit: Slim).

**`product_reviews`** — Customer reviews with star rating and review text.

### Shopping Tables

**`carts`** — One cart per user.

**`cart_items`** — Each item in the cart. Linked to a specific `product_variant_id`, quantity, and the price at the time of adding.

**`wishlists`** — One wishlist per user.

**`wishlist_items`** — Each item in the wishlist.

**`coupons`** — Discount coupons with minimum order value, flat or percentage discount, validity period, and usage limits.

**`coupon_usage`** — Tracks which user used which coupon (prevents double use).

### Order Tables

**`orders`** — Created when the customer places an order. Stores delivery address snapshot, payment method, subtotals, discounts, and total amount.

**`order_items`** — Each product in the order, with price and quantity at the time of purchase (snapshot, not a live reference).

**`order_status_history`** — Tracks status changes (PLACED → CONFIRMED → SHIPPED → DELIVERED).

**`payments`** — Payment record linked to the order. Stores method, status, transaction ID.

**`refunds`** — Refund records for cancelled/returned orders.

**`return_requests`** — Customer return requests.

### Supporting Tables

**`banners`** — Home page banner images.

**`app_settings`** — Key-value application config stored in the DB.

**`audit_logs`** — Logs critical actions for traceability.

**`user_sessions`** — Optional server-side session tracking.

### Entity Relationships (simplified)

```
users ──────────── addresses (one-to-many)
users ──────────── carts (one-to-one)
carts ──────────── cart_items (one-to-many)
cart_items ──────── product_variants (many-to-one)
users ──────────── wishlists (one-to-one)
wishlists ───────── wishlist_items (one-to-many)
users ──────────── orders (one-to-many)
orders ──────────── order_items (one-to-many)
orders ──────────── payments (one-to-one)
products ────────── categories, brands (many-to-one)
products ────────── product_images, product_variants,
                    product_specifications, product_reviews (one-to-many)
```

---

## 6. How a Request Flows Through the App

### Example: Customer adds a product to cart

**Step 1 — Browser sends request**
```
POST /MyntraDemo/cart/add
Body: productId=42, variantId=7, quantity=1
```

**Step 2 — Filter chain runs**
- `FlashMessageFilter` checks for flash messages in session (none here)
- `AuthFilter` checks `/cart/add` — sees `AUTH_USER_ID` in session — allows through

**Step 3 — AddToCartServlet handles the request**
```java
// reads params
long productId = Long.parseLong(request.getParameter("productId"));
// gets userId from session
long userId = (Long) session.getAttribute("AUTH_USER_ID");
// builds a request DTO
AddToCartRequest dto = new AddToCartRequest(userId, productId, variantId, qty);
// calls service
cartService.addToCart(dto);
```

**Step 4 — CartServiceImpl runs business logic**
- Validates the request (positive quantity, etc.)
- Calls `CartDao.addOrUpdateCartItem(dto)`

**Step 5 — JdbcCartDao runs the SQL**
```sql
INSERT INTO cart_items (cart_id, product_variant_id, quantity, unit_price)
VALUES (?, ?, ?, ?)
ON DUPLICATE KEY UPDATE quantity = quantity + ?
```
Uses `PreparedStatement` — no SQL injection possible.

**Step 6 — Response goes back**
- Servlet receives success
- Sets flash message in session: "Item added to cart"
- Sends redirect to `/cart`

**Step 7 — FlashMessageFilter picks up the flash message** on the next request and moves it from session to request attribute so the JSP can display the toast notification.

---

## 7. Security Layer

### Filter Chain

All requests pass through `FlashMessageFilter` first. Then, depending on the URL:

- `/login`, `/register` → `GuestFilter` (redirects logged-in users to home)
- `/home`, `/cart/*`, `/wishlist/*`, `/checkout/*`, `/order/*`, `/profile`, `/address/*` → `AuthFilter` (redirects unauthenticated users to login)
- `/admin/*` → `AdminFilter` (checks for ADMIN role, not just authentication)

The filter mappings are defined in `web.xml`, keeping security configuration in one place.

### Password Security

Passwords go through this flow:

```
Registration:  plaintext → BCrypt.hash(cost=12) → stored in DB
Login:         plaintext + stored hash → BCrypt.verify() → boolean
```

BCrypt cost factor 12 means each hash takes ~300ms on modern hardware — fast enough for users, slow enough to make brute-force attacks impractical.

### Session Management

On successful login:
```java
HttpSession oldSession = request.getSession(false);
if (oldSession != null) {
    oldSession.invalidate();          // prevents session fixation
}
HttpSession session = request.getSession(true);  // brand new session
session.setAttribute("AUTH_USER_ID", userId);
```

The session stores only the minimum needed: `userId`, `fullName`, `email`, `roleName`. Sensitive data (full user object, password hash) is never stored in the session.

### SQL Injection Prevention

Every database query uses `PreparedStatement`:
```java
PreparedStatement stmt = conn.prepareStatement(
    "SELECT * FROM users WHERE email = ?"
);
stmt.setString(1, email);
```

The `?` placeholder separates SQL code from user data — even if a user enters `' OR '1'='1`, it is treated as a literal string, not SQL.

### Database Credentials

No credentials are in the source code. The app reads three environment variables at startup:
```
MYNTRADEMO_DB_URL
MYNTRADEMO_DB_USERNAME
MYNTRADEMO_DB_PASSWORD
```
If any are missing, the app throws an `IllegalStateException` immediately with a clear message — it will not start with missing config.

### Session Cookie Settings (web.xml)
```xml
<session-timeout>30</session-timeout>
<cookie-config>
    <http-only>true</http-only>
</cookie-config>
```
`HttpOnly` means JavaScript cannot access the session cookie — protects against XSS-based session theft.

---

## 8. Module-by-Module Walkthrough

### Authentication Module

**Files:** `LoginServlet`, `RegisterServlet`, `LogoutServlet`, `AuthServiceImpl`, `JdbcUserDao`

**Registration flow:**
1. User submits name, email, phone, password, confirm password
2. `RegisterServlet` builds a `RegisterRequest` DTO
3. `AuthServiceImpl.registerCustomer()` validates all fields using `ValidationUtil` (regex for email, phone, name; length check for password)
4. Checks email uniqueness via `JdbcUserDao.findByEmail()`
5. Hashes password with `PasswordUtil.hashPassword()`
6. Saves user with default CUSTOMER role
7. Redirects to login with a success flash message

**Login flow:**
1. User submits email + password
2. `AuthServiceImpl.login()` loads user by email
3. Calls `PasswordUtil.verifyPassword()` — BCrypt comparison
4. On success, returns an `AuthenticatedUser` DTO
5. Servlet invalidates old session, creates new one, stores user data
6. Redirects to home

---

### Product Catalog Module

**Files:** `ProductListServlet`, `ProductDetailServlet`, `ProductCatalogServiceImpl`, `JdbcProductDao`

**Product listing:**
- Accepts optional query params: `category`, `brand`, `search`, `page`
- `ProductListServlet` builds a `ProductFilterRequest` from these params
- `JdbcProductDao` dynamically builds a WHERE clause based on which filters are active
- Returns a list of `ProductCardDto` (id, name, prices, category, brand, primary image)
- Pagination is handled with SQL `LIMIT` and `OFFSET`

**Product detail:**
- URL: `/product?id=42`
- Loads the full product in multiple JDBC calls (assembled in `JdbcProductDao`):
  - Main product record
  - All images (ordered by `is_primary`, then `sort_order`)
  - All size/colour variants with stock
  - Specifications (key-value pairs)
  - Review summary (average rating, count)
  - Individual reviews
  - Offers
  - Service promises (return policy, delivery time)
  - Seller info
- All assembled into a `ProductDetailDto`

---

### Cart Module

**Files:** `CartPageServlet`, `AddToCartServlet`, etc., `CartServiceImpl`, `JdbcCartDao`

The cart page is the most data-rich page. `CartPageServlet` loads:
- All cart items with current prices, stock levels, and product images
- Applied coupon (validates it is still valid and the minimum order is met)
- Price breakdown: MRP total, item discounts, coupon discount, delivery charge, GST, donation, gift card, final payable amount
- Recommended products
- Delivery address preview (default address)

The `JdbcCartDao` uses an inner `CartItemRecord` (a private static nested class) to hold raw JDBC data before mapping to `CartItemDto`. This avoids multiple database round trips.

---

### Checkout Module

**Files:** `CheckoutAddressServlet`, `CheckoutPaymentServlet`, `PlaceOrderServlet`, `JdbcCheckoutOrderDao`

The checkout is a **three-step flow** using session to carry state between steps:

```
Step 1: /checkout/address
        → User selects delivery address
        → selectedAddressId stored in session

Step 2: /checkout/payment
        → User selects payment method + sub-option
        → Coupon, gift card, donation loaded from session

Step 3: POST /checkout/place-order
        → Reads everything from session
        → Validates cart is not empty
        → Validates address belongs to this user
        → Validates payment method is valid
        → Calls checkoutOrderService.placeOrder()
        → Clears checkout session data
        → Redirects to /order/success?id=...
```

**Order placement is transactional:**
```java
connection.setAutoCommit(false);
try {
    // 1. Insert into orders
    // 2. Insert each cart item into order_items
    // 3. Decrease variant stock for each item
    // 4. Clear cart_items
    // 5. Record coupon usage (if applicable)
    // 6. Insert into payments
    connection.commit();
} catch (SQLException e) {
    connection.rollback();  // all-or-nothing
    throw e;
} finally {
    connection.setAutoCommit(true);
    connection.close();
}
```

If anything fails mid-way (e.g. a variant goes out of stock between cart and checkout), the entire order is rolled back.

---

### Address Module

**Files:** `AddressPageServlet`, `AddAddressServlet`, `UpdateAddressServlet`, `DeleteAddressServlet`, `SetDefaultAddressServlet`, `JdbcAddressDao`

Addresses have ownership enforcement — every DAO operation checks that the `userId` passed in matches the address owner before performing any read or write. A user cannot view or delete another user's address even by guessing the address ID.

---

### Invoice Module

**File:** `OrderInvoiceDownloadServlet`

Generates a PDF invoice on the fly when the customer requests it. The servlet:
1. Loads the full placed order from the database
2. Verifies the order belongs to the logged-in user
3. Builds the PDF in memory using iText/similar
4. Sets response headers to `application/pdf` and `Content-Disposition: attachment`
5. Streams the bytes directly to the browser — no file saved on disk

---

## 9. Key Design Decisions

### Why interfaces everywhere?

Every service and DAO has an interface:
```java
CartService          → interface
CartServiceImpl      → implementation
```

This makes the design open for testing (you can create a `MockCartService` for unit tests without touching a real database) and open for extension (swap MySQL for another DB by writing a new DAO impl without changing the service layer).

### Why a constant class for every category?

Instead of:
```java
request.getSession().setAttribute("authUserId", userId);
```

The code uses:
```java
session.setAttribute(AttributeConstants.AUTH_USER_ID, userId);
```

This eliminates typos, makes refactoring safe (change in one place, compiler catches everywhere else), and makes the code self-documenting.

### Why DTO (Data Transfer Object) pattern?

The database model and what the view needs are rarely identical. A `ProductDetailDto` combines data from 8 different tables into one object the JSP can easily iterate. It also means the view layer never knows SQL exists.

### Why flash messages?

After a POST that redirects (Post-Redirect-Get pattern), you need to show the user a result message on the next page. The `FlashMessageFilter` handles this automatically — any attribute set in the session is moved to the request scope on the next request and then removed from the session. This prevents the message from showing twice.

---

## 10. Known Limitations & Future Improvements

These are honest gaps in the current implementation — good talking points if asked in an interview.

| Limitation | Impact | Planned Fix |
|---|---|---|
| No connection pooling | A new MySQL connection opens on every request — expensive | Add HikariCP; initialize `DataSource` in a `ServletContextListener` |
| No CSRF protection | State-changing POSTs (orders, address delete) vulnerable to cross-site request forgery | Generate per-session token; validate in filter for all POST requests |
| JSP output not escaped | Raw `${variable}` in JSPs — XSS if data contains `<script>` | Wrap all output in `<c:out value="${...}"/>` |
| Empty exception package | Generic `IllegalArgumentException` / `SQLException` bubbles up everywhere | Create custom exceptions: `UserAlreadyExistsException`, `OutOfStockException`, `OrderNotFoundException` |
| No unit tests | Service and DAO logic untested | JUnit 5 + Mockito for service layer; H2 in-memory DB for DAO layer |
| Session cookie not Secure | `<secure>false</secure>` in web.xml | Set to `true` for HTTPS deployments |
| No admin panel | Admin routes are protected by `AdminFilter` but no admin UI exists yet | Build admin dashboard for product/order management |
| No email notifications | No order confirmation email | Integrate JavaMail for transactional emails |
| No real payment gateway | Payment is simulated | Integrate Razorpay or similar |
| Product images are static | SVG placeholders used; no real upload working in production | Complete the file upload integration using Commons FileUpload |

---

*Documentation written for MyntraDemo v1.0.0 — June 2026*
*Developer: Rakesh Gowda B P | rakeshgowdabp05@gmail.com*
