package com.msrv2.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String uniqId;
    private String sku;

    @Field("available") // field is optional
    private boolean isAvailable;

    @Override
    public String toString() {
        return "\n======Product======\n" +
                "{" +
                "uniq_id='" + uniqId + '\'' +
                "sku='" + sku + '\'' +
                ",available='" + isAvailable + '\'' +
                '}' +
                "\n===================\n";
    }
}
