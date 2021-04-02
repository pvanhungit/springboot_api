package com.awesome.service.utility.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class EmailResponse {
    private boolean success;
    private String message;
}
