package com.msrv2.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private String uniqId;
    private String sku;
    private String nameTitle;
    private String description;
    private String listPrice;
    private String salePrice;
    private String category;
    private String categoryTree;
    private String averageProductRating;
    private String productUrl;
    private String productImageUrls;
    private String brand;
    private String totalNumberReviews;
    private String reviews;
    private boolean available;

    @Override
    public String toString() {
        return "\n======Product======\n" +
                "{" +
                "uniq_id='" + uniqId + '\'' +
                ",sku='" + sku + '\'' +
                ", nameTitle='" + nameTitle + '\'' +
                ", description='" + description + '\'' +
                ", listPrice='" + listPrice + '\'' +
                ", category='" + category + '\'' +
                ", isAvailable='" + available + '\'' +
                '}' +
                "\n===================\n";
    }
}
