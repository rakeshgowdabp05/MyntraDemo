(function () {
  "use strict";

  const BODY_LOCK_CLASS = "cart-modal-lock";
  const OPEN_CLASS = "is-open";

  function lockBody() {
    document.body.classList.add(BODY_LOCK_CLASS);
  }

  function unlockBodyIfNoModalOpen() {
    const openedModal = document.querySelector(
      ".cart-modal.is-open, .coupon-drawer.is-open",
    );

    if (!openedModal) {
      document.body.classList.remove(BODY_LOCK_CLASS);
      document.body.classList.remove("coupon-drawer-lock");
    }
  }

  function openModal(modal) {
    if (!modal) {
      return;
    }

    modal.classList.add(OPEN_CLASS);
    modal.setAttribute("aria-hidden", "false");
    lockBody();

    const focusTarget = modal.querySelector(
      "[data-modal-focus], input, textarea, button",
    );

    if (focusTarget) {
      window.setTimeout(function () {
        focusTarget.focus();
      }, 160);
    }
  }

  function closeModal(modal) {
    if (!modal) {
      return;
    }

    modal.classList.remove(OPEN_CLASS);
    modal.setAttribute("aria-hidden", "true");
    unlockBodyIfNoModalOpen();
  }

  function closeAllModals() {
    const modals = document.querySelectorAll(
      ".cart-modal.is-open, .coupon-drawer.is-open",
    );

    modals.forEach(function (modal) {
      modal.classList.remove(OPEN_CLASS);
      modal.setAttribute("aria-hidden", "true");
    });

    document.body.classList.remove(BODY_LOCK_CLASS);
    document.body.classList.remove("coupon-drawer-lock");
  }

  function setupGenericModals() {
    const openButtons = document.querySelectorAll("[data-cart-modal-open]");
    const closeButtons = document.querySelectorAll("[data-cart-modal-close]");

    openButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        const modalName = button.getAttribute("data-cart-modal-open");
        const modal = document.querySelector(
          '[data-cart-modal="' + modalName + '"]',
        );

        openModal(modal);
      });
    });

    closeButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        const modal = button.closest(".cart-modal");

        closeModal(modal);
      });
    });

    document.addEventListener("click", function (event) {
      const backdrop = event.target.closest("[data-cart-modal-backdrop]");

      if (!backdrop) {
        return;
      }

      const modal = backdrop.closest(".cart-modal");
      closeModal(modal);
    });
  }

  function setupLegacyCouponDrawerSupport() {
    const drawer = document.querySelector("[data-coupon-drawer]");
    const openButtons = document.querySelectorAll("[data-coupon-drawer-open]");
    const closeButtons = document.querySelectorAll(
      "[data-coupon-drawer-close]",
    );

    if (!drawer) {
      return;
    }

    function openDrawer() {
      drawer.classList.add(OPEN_CLASS);
      drawer.setAttribute("aria-hidden", "false");
      document.body.classList.add("coupon-drawer-lock");
      lockBody();

      const searchInput = drawer.querySelector("[data-coupon-search]");

      if (searchInput) {
        window.setTimeout(function () {
          searchInput.focus();
        }, 160);
      }
    }

    function closeDrawer() {
      drawer.classList.remove(OPEN_CLASS);
      drawer.setAttribute("aria-hidden", "true");
      unlockBodyIfNoModalOpen();
    }

    openButtons.forEach(function (button) {
      button.addEventListener("click", openDrawer);
    });

    closeButtons.forEach(function (button) {
      button.addEventListener("click", closeDrawer);
    });
  }

  function setupCouponSearch() {
    const searchInputs = document.querySelectorAll("[data-coupon-search]");
    const couponCards = document.querySelectorAll("[data-coupon-card]");

    if (searchInputs.length === 0 || couponCards.length === 0) {
      return;
    }

    function filterCoupons(searchValue) {
      const value = searchValue.trim().toLowerCase();

      couponCards.forEach(function (card) {
        const couponCode = (
          card.getAttribute("data-coupon-code") || ""
        ).toLowerCase();
        const couponTitle = (
          card.getAttribute("data-coupon-title") || ""
        ).toLowerCase();
        const couponDescription = (
          card.getAttribute("data-coupon-description") || ""
        ).toLowerCase();

        const visible =
          value.length === 0 ||
          couponCode.includes(value) ||
          couponTitle.includes(value) ||
          couponDescription.includes(value);

        card.style.display = visible ? "" : "none";
      });
    }

    searchInputs.forEach(function (input) {
      input.addEventListener("input", function () {
        filterCoupons(input.value);
      });
    });
  }

  function setupGiftMessageCounter() {
    const messageBox = document.querySelector("[data-gift-message]");
    const counter = document.querySelector("[data-gift-message-count]");

    if (!messageBox || !counter) {
      return;
    }

    function updateCounter() {
      const maxLength = Number(messageBox.getAttribute("maxlength") || "200");
      const currentLength = messageBox.value.length;
      counter.textContent = currentLength + "/" + maxLength;
    }

    messageBox.addEventListener("input", updateCounter);
    updateCounter();
  }

  function setupOfferCarousel() {
    const carousels = document.querySelectorAll("[data-offer-carousel]");

    carousels.forEach(function (carousel) {
      const track = carousel.querySelector("[data-offer-track]");
      const slides = carousel.querySelectorAll(".cart-myntra-offer-slide");
      const previousButton = carousel.querySelector("[data-offer-prev]");
      const nextButton = carousel.querySelector("[data-offer-next]");
      const dots = document.querySelectorAll("[data-offer-dot]");

      if (!track || slides.length === 0) {
        return;
      }

      let currentIndex = 0;
      let intervalId = null;

      function goTo(index) {
        if (index < 0) {
          currentIndex = slides.length - 1;
        } else if (index >= slides.length) {
          currentIndex = 0;
        } else {
          currentIndex = index;
        }

        track.style.transform = "translateX(-" + currentIndex * 100 + "%)";

        dots.forEach(function (dot, dotIndex) {
          dot.classList.toggle("is-active", dotIndex === currentIndex);
        });
      }

      function next() {
        goTo(currentIndex + 1);
      }

      function previous() {
        goTo(currentIndex - 1);
      }

      function startAutoMove() {
        if (intervalId) {
          window.clearInterval(intervalId);
        }

        if (slides.length > 1) {
          intervalId = window.setInterval(next, 2600);
        }
      }

      function stopAutoMove() {
        if (intervalId) {
          window.clearInterval(intervalId);
          intervalId = null;
        }
      }

      if (previousButton) {
        previousButton.addEventListener("click", function () {
          previous();
          startAutoMove();
        });
      }

      if (nextButton) {
        nextButton.addEventListener("click", function () {
          next();
          startAutoMove();
        });
      }

      dots.forEach(function (dot) {
        dot.addEventListener("click", function () {
          const index = Number(dot.getAttribute("data-offer-dot"));

          if (!Number.isNaN(index)) {
            goTo(index);
            startAutoMove();
          }
        });
      });

      carousel.addEventListener("mouseenter", stopAutoMove);
      carousel.addEventListener("mouseleave", startAutoMove);

      goTo(0);
      startAutoMove();
    });
  }

  function setupDonationCheckbox() {
    const donationCheckbox = document.querySelector("[data-donation-checkbox]");
    const selectedDonationForm = document.querySelector(
      "[data-donation-remove-form]",
    );

    if (!donationCheckbox) {
      return;
    }

    donationCheckbox.addEventListener("change", function () {
      if (!donationCheckbox.checked && selectedDonationForm) {
        selectedDonationForm.submit();
      }
    });
  }

  function setupKeyboardClose() {
    document.addEventListener("keydown", function (event) {
      if (event.key === "Escape") {
        closeAllModals();
      }
    });
  }

  function init() {
    setupGenericModals();
    setupLegacyCouponDrawerSupport();
    setupCouponSearch();
    setupGiftMessageCounter();
    setupOfferCarousel();
    setupDonationCheckbox();
    setupKeyboardClose();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
