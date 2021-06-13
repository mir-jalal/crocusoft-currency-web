package com.crocusoft.currencyconverterweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    private BigDecimal changeRate;
    private String nominal;
    private String code;
    private String name;
    private String description;
    private String type;

    public Currency(String valType, String code) {
        this.type = valType;
        this.code = code;
    }
}
