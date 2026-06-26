(function () {
  "use strict";

  const popularSuggestions = [
    { label: "Men Shirts", url: "/products?section=men&search=shirt" },
    { label: "Men Jackets", url: "/products?section=men&search=jacket" },
    { label: "Women Saree", url: "/products?section=women&search=saree" },
    { label: "Women Dresses", url: "/products?section=women&search=dress" },
    { label: "Kids T-Shirts", url: "/products?section=kids&search=t-shirt" },
    { label: "Running Shoes", url: "/products?search=shoes" },
    { label: "Wallets", url: "/products?search=wallet" },
    { label: "Beauty Products", url: "/products?section=beauty" },
  ];

  const suggestionGroups = {
    men: [
      { label: "Men Shirts", url: "/products?section=men&search=shirt" },
      {
        label: "Men Casual Shirts",
        url: "/products?section=men&search=casual shirt",
      },
      { label: "Men T-Shirts", url: "/products?section=men&search=t-shirt" },
      { label: "Men Jackets", url: "/products?section=men&search=jacket" },
      { label: "Men Wallets", url: "/products?search=wallet" },
      { label: "Men Shoes", url: "/products?search=shoes" },
    ],
    women: [
      { label: "Women Saree", url: "/products?section=women&search=saree" },
      { label: "Women Dresses", url: "/products?section=women&search=dress" },
      { label: "Women Kurta", url: "/products?section=women&search=kurta" },
      { label: "Women Jeans", url: "/products?section=women&search=jeans" },
      { label: "Women Sling Bag", url: "/products?search=sling bag" },
      { label: "Women Sneakers", url: "/products?search=sneakers" },
    ],
    kids: [
      { label: "Kids T-Shirts", url: "/products?section=kids&search=t-shirt" },
      { label: "Kids Joggers", url: "/products?section=kids&search=joggers" },
      {
        label: "Girls Dungaree",
        url: "/products?section=kids&search=dungaree",
      },
      { label: "Boys T-Shirt", url: "/products?section=kids&search=t-shirt" },
    ],
    beauty: [
      { label: "Lipstick", url: "/products?section=beauty&search=lipstick" },
      {
        label: "Moisturizer",
        url: "/products?section=beauty&search=moisturizer",
      },
      {
        label: "Compact Powder",
        url: "/products?section=beauty&search=powder",
      },
      { label: "Makeup", url: "/products?section=beauty&search=makeup" },
    ],
    home: [
      { label: "Bedsheet", url: "/products?section=home&search=bedsheet" },
      { label: "Towels", url: "/products?section=home&search=towels" },
      { label: "Table Lamp", url: "/products?section=home&search=lamp" },
      { label: "Home Decor", url: "/products?section=home" },
    ],
  };

  function getContextPath() {
    const parts = window.location.pathname.split("/");
    return parts.length > 1 ? "/" + parts[1] : "";
  }

  function withContextPath(url) {
    return getContextPath() + url;
  }

  function getSuggestions(keyword) {
    const value = keyword.trim().toLowerCase();

    if (!value) {
      return popularSuggestions;
    }

    if (value === "men" || value.startsWith("men ")) {
      return suggestionGroups.men;
    }

    if (value === "women" || value.startsWith("women ")) {
      return suggestionGroups.women;
    }

    if (
      value === "kids" ||
      value.startsWith("kids ") ||
      value.startsWith("kid ")
    ) {
      return suggestionGroups.kids;
    }

    if (
      value.includes("beauty") ||
      value.includes("makeup") ||
      value.includes("lipstick")
    ) {
      return suggestionGroups.beauty;
    }

    if (
      value.includes("home") ||
      value.includes("bedsheet") ||
      value.includes("lamp")
    ) {
      return suggestionGroups.home;
    }

    return popularSuggestions
      .filter(function (item) {
        return item.label.toLowerCase().includes(value);
      })
      .concat(popularSuggestions)
      .slice(0, 10);
  }

  function renderSuggestions(container, suggestions, hasKeyword) {
    container.innerHTML = "";

    const title = document.createElement("div");
    title.className = "suggestion-title";
    title.textContent = hasKeyword ? "All Others" : "Popular Searches";
    container.appendChild(title);

    const uniqueLabels = new Set();

    suggestions.forEach(function (suggestion) {
      if (uniqueLabels.has(suggestion.label)) {
        return;
      }

      uniqueLabels.add(suggestion.label);

      const link = document.createElement("a");
      link.href = withContextPath(suggestion.url);
      link.textContent = suggestion.label;
      container.appendChild(link);
    });
  }

  function initSearchSuggestions() {
    const form = document.querySelector("[data-search-form]");
    const input = document.querySelector("[data-search-input]");
    const container = document.querySelector("[data-search-suggestions]");

    if (!form || !input || !container) {
      return;
    }

    function openSuggestions() {
      const suggestions = getSuggestions(input.value);
      renderSuggestions(container, suggestions, input.value.trim().length > 0);
      form.classList.add("is-suggestion-open");
    }

    input.addEventListener("focus", openSuggestions);
    input.addEventListener("input", openSuggestions);

    document.addEventListener("click", function (event) {
      if (!form.contains(event.target)) {
        form.classList.remove("is-suggestion-open");
      }
    });

    input.addEventListener("keydown", function (event) {
      if (event.key === "Escape") {
        form.classList.remove("is-suggestion-open");
      }
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initSearchSuggestions);
  } else {
    initSearchSuggestions();
  }
})();
