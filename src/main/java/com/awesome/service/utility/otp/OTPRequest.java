package com.awesome.service.utility.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Data
@AllArgsConstructor
public class OTPRequest {
    private String key;
    private int code;
//    String recipientPhoneNumber;
}
