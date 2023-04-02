package com.wvega.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWS {
    private int code;
    private boolean success;
    private String message;
    private Object data;
}
