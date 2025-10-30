package com.eipl.amcs.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeMultipleResponse {
    private String status;
    private RealTimeError error;
    private List<Map<String, Object>> data;
}
