package com.learnweb.learnweb.exceptoion;

public enum ErrorCode {
    UNCATEGORIZED_EXCEOTION(9999, "UNCATEGORIZED_EXCEOTION"),
    USER_EXITED(1001, "User exited"),
    INVALID_ENUM_KEY(1000,"Invalid key"),
    USERNAME_UNVALID(2001, "User unvalid"),
    PASSWORD_UNVALID(2002, "Password unvalid"),
    USERNAME_NOT_EXITED(2003, "User not exited"),
    UNTHENTICATED(2004, "Unthenticated")


    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
