(function () {
  "use strict";

  const OPEN_CLASS = "is-open";
  const LOCK_CLASS = "order-modal-lock";

  function getModal(selector) {
    return document.querySelector(selector);
  }

  function openModal(modal) {
    if (!modal) {
      console.error("Modal not found");
      return;
    }

    modal.classList.add(OPEN_CLASS);
    modal.setAttribute("aria-hidden", "false");
    document.body.classList.add(LOCK_CLASS);
  }

  function closeModal(modal) {
    if (!modal) {
      return;
    }

    modal.classList.remove(OPEN_CLASS);
    modal.setAttribute("aria-hidden", "true");

    if (!document.querySelector(".order-modal.is-open")) {
      document.body.classList.remove(LOCK_CLASS);
    }
  }

  function closeAllModals() {
    document.querySelectorAll(".order-modal.is-open").forEach(function (modal) {
      modal.classList.remove(OPEN_CLASS);
      modal.setAttribute("aria-hidden", "true");
    });

    document.body.classList.remove(LOCK_CLASS);
  }

  function handleDocumentClick(event) {
    const target = event.target;

    const trackButton = target.closest("[data-track-modal-open]");
    if (trackButton) {
      event.preventDefault();
      openModal(getModal("[data-track-modal]"));
      return;
    }

    const cancelButton = target.closest("[data-cancel-modal-open]");
    if (cancelButton) {
      event.preventDefault();
      openModal(getModal("[data-cancel-modal]"));
      return;
    }

    const addressIntroButton = target.closest("[data-address-intro-open]");
    if (addressIntroButton) {
      event.preventDefault();
      openModal(getModal("[data-address-intro-modal]"));
      return;
    }

    const addressListButton = target.closest("[data-address-list-open]");
    if (addressListButton) {
      event.preventDefault();
      closeModal(getModal("[data-address-intro-modal]"));
      openModal(getModal("[data-address-list-modal]"));
      return;
    }

    const paymentButton = target.closest("[data-payment-modal-open]");
    if (paymentButton) {
      event.preventDefault();
      openModal(getModal("[data-payment-modal]"));
      return;
    }

    const invoiceButton = target.closest("[data-invoice-modal-open]");
    if (invoiceButton) {
      event.preventDefault();
      closeModal(getModal("[data-payment-modal]"));
      openModal(getModal("[data-invoice-modal]"));
      return;
    }

    const closeButton = target.closest("[data-modal-close]");
    if (closeButton) {
      event.preventDefault();
      const modal = closeButton.closest(".order-modal");
      closeModal(modal);
      return;
    }

    const backdrop = target.closest(".order-modal-backdrop");
    if (backdrop) {
      event.preventDefault();
      const modal = backdrop.closest(".order-modal");
      closeModal(modal);
    }
  }

  function protectSubmitButtons() {
    document.querySelectorAll("form").forEach(function (form) {
      form.addEventListener("submit", function () {
        const submitButton = form.querySelector('button[type="submit"]');

        if (submitButton) {
          submitButton.disabled = true;
          submitButton.classList.add("is-submitting");
        }
      });
    });
  }

  function init() {
    document.addEventListener("click", handleDocumentClick);

    document.addEventListener("keydown", function (event) {
      if (event.key === "Escape") {
        closeAllModals();
      }
    });

    protectSubmitButtons();

    console.log("MyntraDemo order details buttons loaded");
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
