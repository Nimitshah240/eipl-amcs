package com.eipl.amcs.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeMultipleResponse<T> {
    private String status;
    private RealTimeError error;
    private List<T> data;
}
