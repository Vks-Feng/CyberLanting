package com.vksfeng.quan.exception;

public class UserNotExistsException extends BaseException{
    public UserNotExistsException(){

    }

    public UserNotExistsException(String msg) {
        super(msg);
    }
}
