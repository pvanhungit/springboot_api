package com.awesome.controller.utils;

import com.awesome.exception.AppException;
import com.awesome.service.user.UserService;
import com.awesome.service.utility.email.EmailResponse;
import com.awesome.service.utility.email.EmailService;
import com.awesome.service.utility.otp.OTPRequest;
import com.awesome.service.utility.otp.OTPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/otp")
public class OTPController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Lazy
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @Autowired
    @Lazy
    private UserService userService;

    @PostMapping("/generate")
    public ResponseEntity<EmailResponse> sendOtp(@RequestBody @NotBlank Long userId) throws AppException {
        //Generate the template to send OTP
        try {
            var email= Optional.ofNullable(userService.findById(userId)).orElse(null);

            if(email.isPresent()){
                // send mail
                String template = emailService.generateUserVerifyMailTemplate(
                        "templates/verify_email.html", String.valueOf(email));
                try {
                    emailService.sendOtpMail(template, String.valueOf(email), "Your secret key to login into futbol");
                    return ResponseEntity.ok(EmailResponse.builder()
                            .success(true)
                            .message("Email sent successfully.")
                            .build());
                } catch (MessagingException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EmailResponse.builder()
                            .success(false)
                            .message(String.format("Failed to send email due to %s", e.getMessage()))
                            .build());
                }
            } else
                throw new AppException(HttpStatus.FORBIDDEN, "EX10002", Arrays.asList(String.format("userID: %s", userId)));
        } catch (Exception e){
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "EX40001", Arrays.asList(e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String,String>> validateOtp(@RequestBody OTPRequest request) throws AppException {
        final String SUCCESS = "Entered Otp is valid";

        Boolean result = otpService.validateOtp(request);

        if(result) {
            Map<String, String> response = new HashMap<>();
            response.put("message", SUCCESS);
            response.put("status", "OK");

            return ResponseEntity.ok().body(response);
        }

        throw new AppException(HttpStatus.FORBIDDEN, "EX50001", Arrays.asList());
    }
}
