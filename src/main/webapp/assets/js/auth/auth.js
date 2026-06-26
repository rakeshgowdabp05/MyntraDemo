(function () {
  "use strict";

  function setupPasswordToggles() {
    document
      .querySelectorAll("[data-password-toggle]")
      .forEach(function (button) {
        const inputId = button.getAttribute("data-password-toggle");
        const input = document.getElementById(inputId);

        if (!input) {
          return;
        }

        button.addEventListener("click", function () {
          const isVisible = input.type === "text";

          input.type = isVisible ? "password" : "text";
          button.classList.toggle("is-visible", !isVisible);
          button.setAttribute(
            "aria-label",
            isVisible ? "Show password" : "Hide password",
          );
        });
      });
  }

  function getPasswordStrength(password) {
    let score = 0;

    if (password.length >= 8) {
      score += 1;
    }

    if (/[A-Z]/.test(password) && /[a-z]/.test(password)) {
      score += 1;
    }

    if (/\d/.test(password)) {
      score += 1;
    }

    if (/[^A-Za-z0-9]/.test(password)) {
      score += 1;
    }

    if (!password) {
      return {
        level: "",
        text: "Use at least 8 characters with letters and numbers.",
      };
    }

    if (score <= 1) {
      return {
        level: "weak",
        text: "Weak password. Add more characters, numbers, or symbols.",
      };
    }

    if (score <= 3) {
      return {
        level: "medium",
        text: "Medium password. Add uppercase letters or symbols to make it stronger.",
      };
    }

    return {
      level: "strong",
      text: "Strong password.",
    };
  }

  function setupPasswordStrength() {
    const passwordInput = document.querySelector("[data-strength-source]");
    const strengthBox = document.querySelector("[data-strength-box]");
    const strengthText = document.querySelector("[data-strength-text]");

    if (!passwordInput || !strengthBox || !strengthText) {
      return;
    }

    passwordInput.addEventListener("input", function () {
      const result = getPasswordStrength(passwordInput.value);

      strengthBox.setAttribute("data-strength", result.level);
      strengthText.textContent = result.text;
    });
  }

  function init() {
    setupPasswordToggles();
    setupPasswordStrength();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
