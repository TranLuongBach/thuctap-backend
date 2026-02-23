package com.example.demo.expection;
//Tạo CustomException
public class ApiException1 extends RuntimeException {

    private final String code;

    public ApiException1(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
