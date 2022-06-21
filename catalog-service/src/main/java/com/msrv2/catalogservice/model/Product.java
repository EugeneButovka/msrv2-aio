package com.msrv2.catalogservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
    @Id
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

    @Override
    public String toString() {
        return "\n======Product======\n" +
                "{" +
                "uniq_id='" + uniqId + '\'' +
                ",sku='" + sku + '\'' +
                ", nameTitle='" + nameTitle + '\'' +
                ", listPrice='" + listPrice + '\'' +
                ", category='" + category + '\'' +
                '}' +
                "\n===================\n";
    }
}
