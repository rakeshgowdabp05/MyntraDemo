(function () {
  "use strict";

  const LOCK_CLASS = "bank-modal-lock";
  const OPEN_CLASS = "is-open";

  function formatAmount(value) {
    const number = Number(value || 0);
    return Math.max(0, Math.round(number)).toString();
  }

  function lockBody() {
    document.body.classList.add(LOCK_CLASS);
  }

  function unlockBody() {
    const openModal = document.querySelector(
      ".bank-modal.is-open, .gift-card-modal.is-open",
    );

    if (!openModal) {
      document.body.classList.remove(LOCK_CLASS);
    }
  }

  function openModal(modal) {
    if (!modal) {
      return;
    }

    modal.classList.add(OPEN_CLASS);
    modal.setAttribute("aria-hidden", "false");
    lockBody();

    const focusTarget = modal.querySelector("input, button");

    if (focusTarget) {
      window.setTimeout(function () {
        focusTarget.focus();
      }, 120);
    }
  }

  function closeModal(modal) {
    if (!modal) {
      return;
    }

    modal.classList.remove(OPEN_CLASS);
    modal.setAttribute("aria-hidden", "true");
    unlockBody();
  }

  function closeAllModals() {
    const modals = document.querySelectorAll(
      ".bank-modal.is-open, .gift-card-modal.is-open",
    );

    modals.forEach(function (modal) {
      modal.classList.remove(OPEN_CLASS);
      modal.setAttribute("aria-hidden", "true");
    });

    document.body.classList.remove(LOCK_CLASS);
  }

  function setupPaymentTabs() {
    const paymentPage = document.querySelector("[data-payment-page]");
    const tabs = document.querySelectorAll("[data-payment-tab]");
    const panels = document.querySelectorAll("[data-payment-panel]");
    const feeRow = document.querySelector("[data-payment-fee-row]");
    const feeLabel = document.querySelector("[data-payment-fee-label]");
    const feeValue = document.querySelector("[data-payment-fee-value]");
    const totalAmount = document.querySelector("[data-payment-total-amount]");

    if (!paymentPage || tabs.length === 0 || panels.length === 0) {
      return;
    }

    const basePayable = Number(
      paymentPage.getAttribute("data-base-payable") || "0",
    );

    function updateSummary(tab) {
      const fee = Number(tab.getAttribute("data-payment-fee") || "0");
      const labelElement = tab.querySelector("strong");
      const label = labelElement ? labelElement.textContent.trim() : "";

      if (feeRow && feeLabel && feeValue) {
        if (fee > 0) {
          feeRow.style.display = "";
          feeLabel.textContent =
            label === "Recommended" ? "Cash On Delivery" : label;
          feeValue.textContent = formatAmount(fee);
        } else {
          feeRow.style.display = "none";
        }
      }

      if (totalAmount) {
        totalAmount.textContent = formatAmount(basePayable + fee);
      }
    }

    function activate(methodCode, tab) {
      tabs.forEach(function (item) {
        item.classList.toggle("is-active", item === tab);
      });

      panels.forEach(function (panel) {
        panel.classList.toggle(
          "is-active",
          panel.getAttribute("data-payment-panel") === methodCode,
        );
      });

      updateSummary(tab);
    }

    tabs.forEach(function (tab) {
      tab.addEventListener("click", function () {
        const methodCode = tab.getAttribute("data-payment-tab");
        activate(methodCode, tab);
      });
    });

    const activeTab =
      document.querySelector("[data-payment-tab].is-active") || tabs[0];

    if (activeTab) {
      activate(activeTab.getAttribute("data-payment-tab"), activeTab);
    }
  }

  function setupPaymentOptions() {
    const optionButtons = document.querySelectorAll("[data-payment-option]");
    const bankButtons = document.querySelectorAll("[data-bank-option]");
    const bankModal = document.querySelector("[data-bank-modal]");
    const selectedBankSummary = document.querySelector(
      "[data-selected-bank-summary]",
    );
    const selectedBankName = document.querySelector(
      "[data-selected-bank-name]",
    );

    function selectOption(button) {
      if (!button || button.disabled) {
        return;
      }

      const methodCode = button.getAttribute("data-method-code");
      const optionCode = button.getAttribute("data-option-code");
      const optionLabel = button.getAttribute("data-option-label") || "";
      const matchingOptions = document.querySelectorAll(
        '[data-payment-option][data-method-code="' + methodCode + '"]',
      );

      matchingOptions.forEach(function (item) {
        item.classList.toggle(
          "is-selected",
          item.getAttribute("data-option-code") === optionCode,
        );
      });

      bankButtons.forEach(function (item) {
        item.classList.toggle(
          "is-selected",
          item.getAttribute("data-option-code") === optionCode,
        );
      });

      const hiddenInput = document.querySelector(
        '[data-payment-hidden-option="' + methodCode + '"]',
      );

      if (hiddenInput) {
        hiddenInput.value = optionCode;
      }

      if (
        methodCode === "NET_BANKING" &&
        selectedBankSummary &&
        selectedBankName
      ) {
        selectedBankSummary.classList.add("is-visible");
        selectedBankName.textContent = optionLabel;
      }
    }

    optionButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        selectOption(button);
      });
    });

    bankButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        selectOption(button);
        closeModal(bankModal);
      });
    });

    optionButtons.forEach(function (button) {
      if (button.classList.contains("is-selected")) {
        selectOption(button);
      }
    });
  }

  function setupPaymentOffers() {
    const carousel = document.querySelector("[data-payment-offer-carousel]");

    if (!carousel) {
      return;
    }

    const track = carousel.querySelector("[data-payment-offer-track]");
    const slides = carousel.querySelectorAll(".payment-offer-card");
    const previousButton = carousel.querySelector("[data-payment-offer-prev]");
    const nextButton = carousel.querySelector("[data-payment-offer-next]");
    const dots = document.querySelectorAll("[data-payment-offer-dot]");

    if (!track || slides.length === 0) {
      return;
    }

    let index = 0;
    let timer = null;

    function goTo(nextIndex) {
      if (nextIndex < 0) {
        index = slides.length - 1;
      } else if (nextIndex >= slides.length) {
        index = 0;
      } else {
        index = nextIndex;
      }

      track.style.transform = "translateX(-" + index * 100 + "%)";

      dots.forEach(function (dot, dotIndex) {
        dot.classList.toggle("is-active", dotIndex === index);
      });
    }

    function next() {
      goTo(index + 1);
    }

    function previous() {
      goTo(index - 1);
    }

    function start() {
      if (timer) {
        window.clearInterval(timer);
      }

      if (slides.length > 1) {
        timer = window.setInterval(next, 2600);
      }
    }

    function stop() {
      if (timer) {
        window.clearInterval(timer);
        timer = null;
      }
    }

    if (previousButton) {
      previousButton.addEventListener("click", function () {
        previous();
        start();
      });
    }

    if (nextButton) {
      nextButton.addEventListener("click", function () {
        next();
        start();
      });
    }

    dots.forEach(function (dot) {
      dot.addEventListener("click", function () {
        const selectedIndex = Number(
          dot.getAttribute("data-payment-offer-dot"),
        );

        if (!Number.isNaN(selectedIndex)) {
          goTo(selectedIndex);
          start();
        }
      });
    });

    carousel.addEventListener("mouseenter", stop);
    carousel.addEventListener("mouseleave", start);

    goTo(0);
    start();
  }

  function setupBankModal() {
    const modal = document.querySelector("[data-bank-modal]");
    const openButtons = document.querySelectorAll("[data-bank-modal-open]");
    const closeButtons = document.querySelectorAll("[data-bank-modal-close]");

    if (!modal) {
      return;
    }

    openButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        openModal(modal);
      });
    });

    closeButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        closeModal(modal);
      });
    });
  }

  function setupGiftCardModal() {
    const modal = document.querySelector("[data-gift-card-modal]");
    const openButtons = document.querySelectorAll(
      "[data-gift-card-modal-open]",
    );
    const closeButtons = document.querySelectorAll(
      "[data-gift-card-modal-close]",
    );

    if (!modal) {
      return;
    }

    openButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        openModal(modal);
      });
    });

    closeButtons.forEach(function (button) {
      button.addEventListener("click", function () {
        closeModal(modal);
      });
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
    setupPaymentTabs();
    setupPaymentOptions();
    setupPaymentOffers();
    setupBankModal();
    setupGiftCardModal();
    setupKeyboardClose();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
