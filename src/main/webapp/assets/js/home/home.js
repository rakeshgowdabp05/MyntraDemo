(function () {
  "use strict";

  function setupHeroCarousel() {
    const hero = document.querySelector("[data-home-hero]");

    if (!hero) {
      return;
    }

    const track = hero.querySelector("[data-hero-track]");
    const slides = hero.querySelectorAll(".hero-slide");
    const previousButton = hero.querySelector("[data-hero-prev]");
    const nextButton = hero.querySelector("[data-hero-next]");
    const dots = hero.querySelectorAll("[data-hero-dot]");

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
        timer = window.setInterval(next, 3200);
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
        const dotIndex = Number(dot.getAttribute("data-hero-dot"));

        if (!Number.isNaN(dotIndex)) {
          goTo(dotIndex);
          start();
        }
      });
    });

    hero.addEventListener("mouseenter", stop);
    hero.addEventListener("mouseleave", start);

    goTo(0);
    start();
  }

  function init() {
    setupHeroCarousel();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
