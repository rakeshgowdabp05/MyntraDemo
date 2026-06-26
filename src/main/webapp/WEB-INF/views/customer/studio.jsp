<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Studio - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css" />
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css" />
    <link rel="stylesheet" href="${contextPath}/assets/css/home/studio.css" />
  </head>
  <body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %> <%@ include
    file="/WEB-INF/views/common/toast.jsp" %>

    <main class="studio-page">
      <section class="studio-hero">
        <div>
          <span>MYNTRADEMO STUDIO</span>
          <h1>Your daily fashion inspiration</h1>
          <p>
            Explore fresh styles, new arrivals, curated picks, and trending
            fashion from your live product catalog.
          </p>
          <a href="${contextPath}/products?section=genz"
            >EXPLORE NEW ARRIVALS</a
          >
        </div>
      </section>

      <section class="studio-grid">
        <a
          href="${contextPath}/products?section=men"
          class="studio-card studio-men"
        >
          <div>
            <strong>Men's Trends</strong>
            <span>Explore</span>
          </div>
        </a>

        <a
          href="${contextPath}/products?section=women"
          class="studio-card studio-women"
        >
          <div>
            <strong>Women's Trends</strong>
            <span>Explore</span>
          </div>
        </a>

        <a
          href="${contextPath}/products?section=kids"
          class="studio-card studio-kids"
        >
          <div>
            <strong>Kids Collection</strong>
            <span>Explore</span>
          </div>
        </a>

        <a
          href="${contextPath}/products?section=genz"
          class="studio-card studio-latest"
        >
          <div>
            <strong>Latest Drops</strong>
            <span>Explore</span>
          </div>
        </a>
      </section>
    </main>

    <script src="${contextPath}/assets/js/common/toast.js"></script>
  </body>
</html>
