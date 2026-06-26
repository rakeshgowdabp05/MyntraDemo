(function () {
  "use strict";

  function initSearchShortcut() {
    const searchInput = document.querySelector(
      ".orders-search-form input[name='search']",
    );

    if (!searchInput) {
      return;
    }

    searchInput.addEventListener("keydown", function (event) {
      if (event.key === "Escape") {
        searchInput.value = "";
      }
    });
  }

  function init() {
    initSearchShortcut();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
