package com.eipl.amcs.base.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MyResponse<T> {
    private String status;
    private ErrorMessage error;
    private T data;
}
