package com.awesome.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {
    private static final Map<String, String> errorCode;

    static {
        errorCode = new HashMap<>();

        // User
        errorCode.put("EX10001", "user.exists");
        errorCode.put("EX10002", "user.not_found");
        errorCode.put("EX60003", "user.insert_failed_keycloak");
        errorCode.put("EX60004", "user.insert_failed");

        // Post
        errorCode.put("EX20001", "post.not_found");

        // Mail service
        errorCode.put("EX40001", "mail.error");

        // Auth, Verification
        errorCode.put("EX50001", "token.invalid");
        errorCode.put("EX50002", "otp.invalid");
        errorCode.put("EX50003", "action.unauthorized");
        errorCode.put("EX50004", "sso.roleNotFound");
        errorCode.put("EX50005", "sso.roleMemberGreaterThanOne");

        // Validation
        errorCode.put("EX60001", "invalid.params");
    }

    public static String getMessageKey(String code){
        String key = errorCode.get(code);
        return key == null ? "key does not exist" : key;
    }
}
