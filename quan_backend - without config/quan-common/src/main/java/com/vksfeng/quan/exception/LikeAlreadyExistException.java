package com.vksfeng.quan.exception;

public class LikeAlreadyExistException extends BaseException{
    public LikeAlreadyExistException() {
        super("点赞已存在");
    }

    public LikeAlreadyExistException(String msg) {
        super(msg);
    }
}
