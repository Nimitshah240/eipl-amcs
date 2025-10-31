package com.eipl.amcs.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypeKeyValDto {
    private short key;
    private String value;
    private boolean localSale;
    private boolean productSale;
    private boolean customerTypeCreate;

    @Override
    public String toString() {
        return value;
    }
}
