<div align="center">

# 🛍️ MyntraDemo

### A Production-Style Java MVC E-Commerce Web Application

<p>
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jakarta_Servlet-6.0-005571?style=for-the-badge&logo=jakarta&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-9.1.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Apache_Tomcat-10.1.x-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black"/>
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"/>
</p>

<p>
  <img src="https://img.shields.io/badge/Status-In%20Development-orange?style=flat-square"/>
  <img src="https://img.shields.io/badge/Java%20Version-21-blue?style=flat-square"/>
  <img src="https://img.shields.io/badge/Architecture-MVC-green?style=flat-square"/>
  <img src="https://img.shields.io/badge/Database-MySQL-blue?style=flat-square"/>
</p>

</div>

---

## 📌 About

MyntraDemo is a full-stack Java MVC e-commerce web application inspired by modern fashion shopping platforms like Myntra. Built entirely without Spring Boot, React, or Bootstrap — it uses raw Servlets, JSP, JDBC, and MySQL to demonstrate a clean, layered Java web architecture from scratch.

The project covers the complete customer journey: browsing products, managing a wishlist and cart, applying coupons and gift cards, going through a multi-step checkout, placing orders, downloading invoices, and managing a profile and addresses — all backed by a real relational database.

> **Goal:** Demonstrate production-style Java web development skills to recruiters and interviewers — clean architecture, proper security, real database integration, and a polished UI.

---

## 🚀 Live Demo & Repository

| | Link |
|---|---|
| 🔗 GitHub | [github.com/rakeshgowdabp05/MyntraDemo](https://github.com/rakeshgowdabp05/MyntraDemo) |
| 🌐 Live Demo | Deployment in progress |
| 👤 Developer | [Rakesh Gowda B P](https://www.linkedin.com/in/rakesh-gowda-bp) |

---

## ✨ Features

### 🔐 Authentication
- Customer registration with full validation (email, phone, password rules)
- Secure login with BCrypt password hashing (cost factor 12)
- Session fixation prevention — old session invalidated on login
- Role-based access control (Customer / Admin)
- AuthFilter, GuestFilter, and AdminFilter for route protection
- No-cache headers on authenticated pages (back-button protection)

### 🏠 Home & Catalog
- Dynamic home page with featured categories, brands, and products
- Product listing page with category and brand filtering
- Search-based product discovery
- Product detail page with image gallery, variants (size/colour), reviews, offers, and service promises
- Studio page

### 🛒 Cart
- Add to cart with size/variant selection
- Update item quantity
- Remove items
- Move items to wishlist from cart
- Coupon code application and removal
- Gift card application and removal
- Donation toggle (round-up feature)
- Gift packaging toggle
- Delivery address preview on cart page
- Real-time price breakdown (MRP, discount, delivery, tax, total)

### ❤️ Wishlist
- Add/remove products
- Move wishlist items to cart
- Recommended products section

### 📦 Checkout & Orders
- Multi-step checkout: Address → Payment → Confirm
- Address selection and management during checkout
- Payment method selection (COD, UPI, Card, Netbanking, Wallets)
- Payment option sub-selection
- Order placement with full price recalculation
- Order success page
- My Orders page with status tracking
- Detailed order view with item breakdown
- Order cancellation
- PDF invoice download
- Update delivery address after order placement

### 👤 Profile & Account
- View and update profile (name, email, phone)
- Address book management (add, edit, set default, delete)
- Account features and settings page
- Help center and help chat

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Web Framework | Jakarta Servlet API 6.0 |
| View Layer | JSP + JSTL 3.0 |
| Database Access | JDBC (raw, no ORM) |
| Build Tool | Maven |
| Server | Apache Tomcat 10.1.x |
| Database | MySQL 9.1.0 |
| Password Hashing | BCrypt (at.favre.lib, cost 12) |
| File Upload | Apache Commons FileUpload2 |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Dev Tools | VS Code, MySQL Workbench, Postman, Git |

---

## 🗂️ Project Structure

```
MyntraDemo/
├── database/
│   ├── schema-current.sql          ← Full DB schema (26 tables)
│   ├── seed-products.sql           ← Initial product seed data
│   ├── seed-more-products-30.sql   ← Extended product data
│   ├── product-detail-module.sql   ← Product detail data
│   ├── checkout-payment-module.sql ← Payment method seed data
│   └── address-coupon-module.sql   ← Address and coupon data
│
├── src/main/java/com/myntrademo/
│   ├── constant/       ← All magic-string constants (routes, messages, attributes)
│   ├── controller/
│   │   ├── auth/       ← Login, Register, Logout servlets
│   │   └── customer/   ← ~35 customer-facing servlets
│   ├── dao/            ← DAO interfaces
│   │   └── impl/       ← JDBC DAO implementations
│   ├── dto/            ← Data Transfer Objects (catalog, cart, checkout, home, etc.)
│   ├── filter/         ← AuthFilter, GuestFilter, AdminFilter, FlashMessageFilter
│   ├── model/          ← Domain model (User)
│   ├── service/        ← Service interfaces
│   │   └── impl/       ← Service implementations
│   └── util/           ← DBConnection, PasswordUtil, ValidationUtil
│
├── src/main/webapp/
│   ├── assets/
│   │   ├── css/        ← Page-specific stylesheets
│   │   ├── js/         ← Page-specific JavaScript
│   │   └── images/     ← Logo and product images
│   └── WEB-INF/
│       ├── views/      ← JSP pages (auth, common, customer)
│       └── web.xml     ← Servlet config, filters, session settings
│
└── pom.xml
```

---

## 🗃️ Database Schema

The database has **26 tables** covering the full e-commerce domain:

`users` · `roles` · `user_sessions` · `addresses` · `products` · `categories` · `brands` · `product_images` · `product_variants` · `product_specifications` · `product_reviews` · `wishlists` · `wishlist_items` · `carts` · `cart_items` · `coupons` · `coupon_usage` · `orders` · `order_items` · `order_status_history` · `payments` · `refunds` · `return_requests` · `banners` · `app_settings` · `audit_logs`

---

## ⚙️ Local Setup

### Prerequisites

- Java 21+
- Apache Tomcat 10.1.x
- MySQL 8.x
- Maven 3.8+

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/rakeshgowdabp05/MyntraDemo.git
cd MyntraDemo
```

**2. Set up the database**
```sql
CREATE DATABASE myntrademo_db;
USE myntrademo_db;
SOURCE database/schema-current.sql;
SOURCE database/seed-products.sql;
SOURCE database/seed-more-products-30.sql;
SOURCE database/product-detail-module.sql;
SOURCE database/checkout-payment-module.sql;
SOURCE database/address-coupon-module.sql;
```

**3. Set environment variables**

On Windows (PowerShell):
```powershell
$env:MYNTRADEMO_DB_URL      = "jdbc:mysql://localhost:3306/myntrademo_db"
$env:MYNTRADEMO_DB_USERNAME = "root"
$env:MYNTRADEMO_DB_PASSWORD = "your_password"
```

On Linux/Mac:
```bash
export MYNTRADEMO_DB_URL="jdbc:mysql://localhost:3306/myntrademo_db"
export MYNTRADEMO_DB_USERNAME="root"
export MYNTRADEMO_DB_PASSWORD="your_password"
```

**4. Build the project**
```bash
mvn clean package
```

**5. Deploy**

Copy `target/MyntraDemo.war` to your Tomcat `webapps/` folder and start Tomcat, or use the included PowerShell script:
```powershell
.\start-tomcat.ps1
```

**6. Open in browser**
```
http://localhost:8080/MyntraDemo/home
```

---

## 🔒 Security Highlights

- Passwords hashed with **BCrypt** (cost factor 12) — never stored in plaintext
- **Session fixation prevention** — old session invalidated on every login
- **Environment variable** based DB config — no credentials in source code
- **PreparedStatement** throughout — SQL injection prevented
- **HttpOnly** session cookies — JavaScript cannot access session token
- **No-cache headers** on authenticated pages — browser back-button safe
- **Role-based filters** — Admin routes protected by AdminFilter, customer routes by AuthFilter

---

## 📐 Architecture

```
Browser  ──HTTP──▶  Servlet (Controller)
                         │
                    Service Layer
                         │
                     DAO Layer
                         │
                  MySQL Database
                         │
              ◀── Data ──┘
                         │
                    JSP Views
                    CSS + JS
```

The project strictly follows the **MVC + DAO + Service** layered pattern:
- **Controller** (Servlet) — handles HTTP, reads request params, calls service, forwards to view
- **Service** — business logic, validation, orchestration
- **DAO** — all SQL via PreparedStatement, returns DTOs
- **View** (JSP + JSTL) — renders the response, no business logic

---

## 👨‍💻 Developer

**Rakesh Gowda B P**
Final Year B.E. CSE | Dr. SMCE, Nelamangala (VTU) | CGPA: 8.38
Java Full Stack Intern @ Tap Edtech Pvt Ltd

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=flat-square&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rakesh-gowda-bp)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)](https://github.com/rakeshgowdabp05)
[![Portfolio](https://img.shields.io/badge/Portfolio-000000?style=flat-square&logo=firefox&logoColor=white)](https://demo-portfolio-theta-ten.vercel.app)
[![Gmail](https://img.shields.io/badge/Gmail-EA4335?style=flat-square&logo=gmail&logoColor=white)](mailto:rakeshgowdabp05@gmail.com)

---

<div align="center">
  <i>Built with Java, JDBC, JSP, MySQL — no shortcuts, no frameworks, just clean code.</i>
</div>
