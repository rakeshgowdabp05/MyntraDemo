package com.myntrademo.dto.catalog;

import java.math.BigDecimal;

public class ProductReviewSummaryDto {

    private BigDecimal averageRating;
    private Integer totalReviews;
    private Integer fiveStarCount;
    private Integer fourStarCount;
    private Integer threeStarCount;
    private Integer twoStarCount;
    private Integer oneStarCount;

    public ProductReviewSummaryDto() {
        this.averageRating = BigDecimal.ZERO;
        this.totalReviews = 0;
        this.fiveStarCount = 0;
        this.fourStarCount = 0;
        this.threeStarCount = 0;
        this.twoStarCount = 0;
        this.oneStarCount = 0;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Integer getFiveStarCount() {
        return fiveStarCount;
    }

    public void setFiveStarCount(Integer fiveStarCount) {
        this.fiveStarCount = fiveStarCount;
    }

    public Integer getFourStarCount() {
        return fourStarCount;
    }

    public void setFourStarCount(Integer fourStarCount) {
        this.fourStarCount = fourStarCount;
    }

    public Integer getThreeStarCount() {
        return threeStarCount;
    }

    public void setThreeStarCount(Integer threeStarCount) {
        this.threeStarCount = threeStarCount;
    }

    public Integer getTwoStarCount() {
        return twoStarCount;
    }

    public void setTwoStarCount(Integer twoStarCount) {
        this.twoStarCount = twoStarCount;
    }

    public Integer getOneStarCount() {
        return oneStarCount;
    }

    public void setOneStarCount(Integer oneStarCount) {
        this.oneStarCount = oneStarCount;
    }

    public boolean hasReviews() {
        return totalReviews != null && totalReviews > 0;
    }

    public int getFiveStarPercent() {
        return calculatePercent(fiveStarCount);
    }

    public int getFourStarPercent() {
        return calculatePercent(fourStarCount);
    }

    public int getThreeStarPercent() {
        return calculatePercent(threeStarCount);
    }

    public int getTwoStarPercent() {
        return calculatePercent(twoStarCount);
    }

    public int getOneStarPercent() {
        return calculatePercent(oneStarCount);
    }

    private int calculatePercent(Integer count) {
        if (count == null || totalReviews == null || totalReviews == 0) {
            return 0;
        }

        return Math.round((count * 100.0f) / totalReviews);
    }
}
