package com.vksfeng.quan.exception;

public class FriendshipNotExistException extends BaseException{

    public FriendshipNotExistException() {
        super("好友关系不存在");
    }

    public FriendshipNotExistException(String msg) {
        super(msg);
    }
}
