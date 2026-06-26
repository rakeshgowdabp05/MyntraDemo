<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Create Account - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css" />
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css" />
    <link rel="stylesheet" href="${contextPath}/assets/css/auth/auth.css" />
  </head>
  <body>
    <%@ include file="/WEB-INF/views/common/toast.jsp" %>

    <main class="auth-page">
      <section class="auth-shell auth-shell-register">
        <aside class="auth-info-panel">
          <header class="auth-brand-row">
            <a
              href="${contextPath}/products"
              class="auth-logo"
              aria-label="Go to products"
              >MD</a
            >

            <div class="auth-brand-copy">
              <strong>MyntraDemo</strong>
              <span>Java MVC Ecommerce</span>
            </div>
          </header>

          <section class="auth-hero-copy">
            <span>CREATE ACCOUNT</span>
            <h1>
              Create your shopping profile and keep every action connected.
            </h1>
            <p>
              Register once to save wishlist items, manage your bag, and prepare
              your account for checkout.
            </p>
          </section>

          <section class="auth-trust-grid">
            <article>
              <div class="auth-trust-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path
                    d="M12 12.2a4.8 4.8 0 1 0 0-9.6 4.8 4.8 0 0 0 0 9.6Zm0-1.8a3 3 0 1 1 0-6 3 3 0 0 1 0 6Zm8 10.6c-.4-4-3.8-7-8-7s-7.6 3-8 7h2c.4-2.8 2.9-5 6-5s5.6 2.2 6 5h2Z"
                  />
                </svg>
              </div>
              <div>
                <h2>Personal profile</h2>
                <p>
                  Your shopping data stays connected with your user account.
                </p>
              </div>
            </article>

            <article>
              <div class="auth-trust-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path
                    d="M7 7V6a5 5 0 0 1 10 0v1h2.2l1 14H3.8l1-14H7Zm2 0h6V6a3 3 0 0 0-6 0v1Zm-2.3 2-.7 10h12l-.7-10H6.7Z"
                  />
                </svg>
              </div>
              <div>
                <h2>Real cart flow</h2>
                <p>Bag and wishlist actions use MySQL-backed records.</p>
              </div>
            </article>

            <article>
              <div class="auth-trust-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path
                    d="M4 4h16v16H4V4Zm2 2v12h12V6H6Zm2 2h8v2H8V8Zm0 4h8v2H8v-2Z"
                  />
                </svg>
              </div>
              <div>
                <h2>Checkout support</h2>
                <p>Ready for address, coupon, payment, and order modules.</p>
              </div>
            </article>

            <article>
              <div class="auth-trust-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M4 5h16v3H4V5Zm0 5h16v3H4v-3Zm0 5h16v3H4v-3Z" />
                </svg>
              </div>
              <div>
                <h2>Clean MVC</h2>
                <p>Servlet, service, DAO, DTO, JSP, and MySQL structure.</p>
              </div>
            </article>
          </section>

          <footer class="auth-left-footer">
            <strong>Production-style project structure</strong>
            <span>Clean frontend with backend-ready ecommerce modules.</span>
          </footer>
        </aside>

        <section class="auth-form-panel">
          <div class="auth-form-wrap">
            <header class="auth-form-head">
              <span>REGISTER</span>
              <h2>Create your account</h2>
              <p>
                Enter your details to start your connected shopping experience.
              </p>
            </header>

            <section class="auth-social-grid">
              <a href="${contextPath}/oauth/google" class="auth-social-btn">
                <span class="auth-social-icon">
                  <svg viewBox="0 0 48 48" aria-hidden="true">
                    <path
                      fill="#FFC107"
                      d="M43.6 20.5H42V20H24v8h11.3C33.7 32.7 29.3 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3.1 0 5.9 1.2 8 3.1l5.7-5.7C34.1 6.1 29.3 4 24 4 12.9 4 4 12.9 4 24s8.9 20 20 20 20-8.9 20-20c0-1.3-.1-2.4-.4-3.5z"
                    />
                    <path
                      fill="#FF3D00"
                      d="M6.3 14.7l6.6 4.8C14.7 15.1 19 12 24 12c3.1 0 5.9 1.2 8 3.1l5.7-5.7C34.1 6.1 29.3 4 24 4 16.3 4 9.7 8.3 6.3 14.7z"
                    />
                    <path
                      fill="#4CAF50"
                      d="M24 44c5.2 0 9.9-2 13.4-5.2l-6.2-5.2C29.2 35.1 26.7 36 24 36c-5.2 0-9.6-3.3-11.3-7.8l-6.5 5C9.5 39.6 16.2 44 24 44z"
                    />
                    <path
                      fill="#1976D2"
                      d="M43.6 20.5H42V20H24v8h11.3c-.8 2.3-2.3 4.2-4.1 5.6l6.2 5.2C36.9 39.2 44 34 44 24c0-1.3-.1-2.4-.4-3.5z"
                    />
                  </svg>
                </span>
                <span>Continue with Google</span>
              </a>

              <a
                href="${contextPath}/oauth/apple"
                class="auth-social-btn auth-apple-btn"
              >
                <span class="auth-social-icon">
                  <svg viewBox="0 0 24 24" aria-hidden="true">
                    <path
                      d="M16.37 12.24c.02 2.18 1.91 2.91 1.93 2.92-.02.05-.3 1.03-.99 2.03-.59.87-1.2 1.74-2.17 1.76-.95.02-1.25-.56-2.33-.56-1.08 0-1.42.54-2.31.58-.93.03-1.63-.93-2.23-1.8-1.23-1.77-2.16-5.01-.9-7.21.62-1.09 1.73-1.78 2.94-1.8.92-.02 1.79.62 2.33.62.54 0 1.56-.76 2.63-.65.45.02 1.72.18 2.54 1.38-.07.04-1.51.88-1.49 2.73zM14.63 4.88c.49-.6.82-1.44.73-2.28-.71.03-1.57.48-2.08 1.08-.46.53-.87 1.38-.76 2.19.8.06 1.62-.41 2.11-.99z"
                    />
                  </svg>
                </span>
                <span>Continue with Apple</span>
              </a>
            </section>

            <div class="auth-divider">
              <span>or create account with email</span>
            </div>

            <form
              method="post"
              action="${contextPath}/register"
              class="auth-form"
            >
              <label class="auth-field">
                <span>Full name</span>
                <input
                  type="text"
                  name="fullName"
                  value="${param.fullName}"
                  placeholder="Enter full name"
                  autocomplete="name"
                  required
                />
              </label>

              <label class="auth-field">
                <span>Email address</span>
                <input
                  type="email"
                  name="email"
                  value="${param.email}"
                  placeholder="you@example.com"
                  autocomplete="email"
                  required
                />
              </label>

              <label class="auth-field">
                <span>Phone number</span>
                <input
                  type="tel"
                  name="phone"
                  value="${param.phone}"
                  placeholder="Enter phone number"
                  autocomplete="tel"
                  required
                />
              </label>

              <label class="auth-field">
                <span>Password</span>
                <div class="auth-password-box">
                  <input
                    id="registerPassword"
                    type="password"
                    name="password"
                    placeholder="Create password"
                    autocomplete="new-password"
                    data-strength-source
                    required
                  />
                  <button
                    type="button"
                    class="auth-password-toggle"
                    data-password-toggle="registerPassword"
                    aria-label="Show password"
                  >
                    <svg
                      class="eye-open"
                      viewBox="0 0 24 24"
                      aria-hidden="true"
                    >
                      <path
                        d="M12 5c5.4 0 9 5.4 9 7s-3.6 7-9 7-9-5.4-9-7 3.6-7 9-7Zm0 2c-3.9 0-6.7 3.8-7 5 .3 1.2 3.1 5 7 5s6.7-3.8 7-5c-.3-1.2-3.1-5-7-5Zm0 2.2A2.8 2.8 0 1 1 12 14.8 2.8 2.8 0 0 1 12 9.2Z"
                      />
                    </svg>
                    <svg
                      class="eye-closed"
                      viewBox="0 0 24 24"
                      aria-hidden="true"
                    >
                      <path
                        d="M3.3 2 22 20.7 20.7 22l-3.1-3.1A10.2 10.2 0 0 1 12 21c-5.4 0-9-5.4-9-7 0-.9 1.2-2.9 3.1-4.6L2 5.3 3.3 2Zm8.7 5c5.4 0 9 5.4 9 7 0 .7-.8 2.1-2.1 3.4l-1.4-1.4c.9-.9 1.4-1.7 1.5-2-.3-1.2-3.1-5-7-5-.8 0-1.6.2-2.3.5L8.2 8C9.3 7.4 10.6 7 12 7Z"
                      />
                    </svg>
                  </button>
                </div>
              </label>

              <div class="auth-strength" data-strength-box>
                <div class="auth-strength-track">
                  <span data-strength-fill></span>
                </div>
                <p data-strength-text>
                  Use at least 8 characters with letters and numbers.
                </p>
              </div>

              <label class="auth-field">
                <span>Confirm password</span>
                <div class="auth-password-box">
                  <input
                    id="confirmPassword"
                    type="password"
                    name="confirmPassword"
                    placeholder="Confirm password"
                    autocomplete="new-password"
                    required
                  />
                  <button
                    type="button"
                    class="auth-password-toggle"
                    data-password-toggle="confirmPassword"
                    aria-label="Show password"
                  >
                    <svg
                      class="eye-open"
                      viewBox="0 0 24 24"
                      aria-hidden="true"
                    >
                      <path
                        d="M12 5c5.4 0 9 5.4 9 7s-3.6 7-9 7-9-5.4-9-7 3.6-7 9-7Zm0 2c-3.9 0-6.7 3.8-7 5 .3 1.2 3.1 5 7 5s6.7-3.8 7-5c-.3-1.2-3.1-5-7-5Zm0 2.2A2.8 2.8 0 1 1 12 14.8 2.8 2.8 0 0 1 12 9.2Z"
                      />
                    </svg>
                    <svg
                      class="eye-closed"
                      viewBox="0 0 24 24"
                      aria-hidden="true"
                    >
                      <path
                        d="M3.3 2 22 20.7 20.7 22l-3.1-3.1A10.2 10.2 0 0 1 12 21c-5.4 0-9-5.4-9-7 0-.9 1.2-2.9 3.1-4.6L2 5.3 3.3 2Zm8.7 5c5.4 0 9 5.4 9 7 0 .7-.8 2.1-2.1 3.4l-1.4-1.4c.9-.9 1.4-1.7 1.5-2-.3-1.2-3.1-5-7-5-.8 0-1.6.2-2.3.5L8.2 8C9.3 7.4 10.6 7 12 7Z"
                      />
                    </svg>
                  </button>
                </div>
              </label>

              <button type="submit" class="auth-submit-btn">
                CREATE ACCOUNT
              </button>
            </form>

            <footer class="auth-bottom">
              <p>
                By creating an account, you agree to MyntraDemo’s
                <a href="${contextPath}/terms">Terms of Use</a>
                and
                <a href="${contextPath}/privacy">Privacy Policy</a>.
              </p>

              <div class="auth-switch">
                <span>Already have an account?</span>
                <a href="${contextPath}/login">Sign in</a>
              </div>

              <a href="${contextPath}/products" class="auth-help-link"
                >Explore products first</a
              >
            </footer>
          </div>
        </section>
      </section>
    </main>

    <script src="${contextPath}/assets/js/common/toast.js"></script>
    <script src="${contextPath}/assets/js/auth/auth.js"></script>
  </body>
</html>
