(function () {
  "use strict";

  const TOAST_TIMEOUT = 3600;

  function getRoot() {
    let root = document.getElementById("mdToastRoot");

    if (!root) {
      root = document.createElement("div");
      root.id = "mdToastRoot";
      root.className = "md-toast-root";
      document.body.appendChild(root);
    }

    return root;
  }

  function cleanMessage(message) {
    if (message === null || message === undefined) {
      return "";
    }

    return String(message).trim();
  }

  function iconFor(type) {
    if (type === "error") {
      return "!";
    }

    if (type === "info") {
      return "i";
    }

    return "✓";
  }

  function showToast(type, message) {
    const finalMessage = cleanMessage(message);

    if (!finalMessage) {
      return;
    }

    const root = getRoot();
    const toast = document.createElement("div");
    const toastType = type === "error" || type === "info" ? type : "success";

    toast.className = "md-toast md-toast-" + toastType;
    toast.setAttribute("role", toastType === "error" ? "alert" : "status");

    const bar = document.createElement("span");
    bar.className = "md-toast-bar";

    const icon = document.createElement("span");
    icon.className = "md-toast-icon";
    icon.textContent = iconFor(toastType);

    const text = document.createElement("div");
    text.className = "md-toast-message";
    text.textContent = finalMessage;

    const close = document.createElement("button");
    close.type = "button";
    close.className = "md-toast-close";
    close.textContent = "Close";
    close.setAttribute("aria-label", "Close notification");

    toast.appendChild(bar);
    toast.appendChild(icon);
    toast.appendChild(text);
    toast.appendChild(close);

    root.appendChild(toast);

    function removeToast() {
      toast.classList.add("is-hiding");

      window.setTimeout(function () {
        if (toast.parentNode) {
          toast.parentNode.removeChild(toast);
        }
      }, 180);
    }

    close.addEventListener("click", removeToast);
    window.setTimeout(removeToast, TOAST_TIMEOUT);
  }

  function showInitialMessages() {
    const root = document.getElementById("mdToastRoot");

    if (!root) {
      return;
    }

    showToast("success", root.dataset.success);
    showToast("error", root.dataset.error);
    showToast("info", root.dataset.info);

    root.dataset.success = "";
    root.dataset.error = "";
    root.dataset.info = "";
  }

  window.MyntraDemoToast = {
    success: function (message) {
      showToast("success", message);
    },
    error: function (message) {
      showToast("error", message);
    },
    info: function (message) {
      showToast("info", message);
    },
  };

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", showInitialMessages);
  } else {
    showInitialMessages();
  }
})();
