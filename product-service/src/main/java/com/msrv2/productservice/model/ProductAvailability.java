package com.msrv2.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductAvailability {
    private String uniqId;
    private String sku;
    private boolean available;

    @Override
    public String toString() {
        return "\n======Product======\n" +
                "{" +
                "uniq_id='" + uniqId + '\'' +
                "sku='" + sku + '\'' +
                ",available='" + available + '\'' +
                '}' +
                "\n===================\n";
    }
}
