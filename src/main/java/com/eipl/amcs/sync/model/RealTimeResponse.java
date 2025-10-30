package com.eipl.amcs.sync.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class RealTimeResponse {
    private String status;
    private RealTimeError error;
    private Map<String, Object> data;
}
