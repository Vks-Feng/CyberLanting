package com.vksfeng.quan.exception;

public class BaseException extends RuntimeException{

    BaseException() { }

    BaseException(String msg) {
        super(msg);
    }
}
