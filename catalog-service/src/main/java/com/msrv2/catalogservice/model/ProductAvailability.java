package com.msrv2.catalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class ProductAvailability {
    @Id
    private String uniqId;
    private Boolean available;

    @Override
    public String toString() {
        return "\n======Product======\n" +
                "{" +
                "uniq_id='" + uniqId + '\'' +
                ",available='" + available + '\'' +
                '}' +
                "\n===================\n";
    }
}
