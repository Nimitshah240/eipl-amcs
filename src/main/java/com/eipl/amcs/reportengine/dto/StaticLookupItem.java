package com.eipl.amcs.reportengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaticLookupItem {

    private String value;
    private String display;

    @Override
    public String toString() {
        return display;
    }
}