package com.awesome.service.utility.email;

import com.awesome.common.EmailTemplate;
import com.awesome.common.utils.CipherUtil;
import com.awesome.exception.AppException;
import com.awesome.service.user.UserService;
import com.awesome.service.utility.otp.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String EMAIL_ADDRESS_IS_NOT_VERIFIED = "Email address is not verified";

    @Value("${spring.mail.username}")
    private String SENDER_ADDRESS;

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private OTPService otpService;

    //Generate the template to send verification OTP
    public String generateUserVerifyMailTemplate(String templateFile, String email) throws Exception {
        var key = CipherUtil.encode(email);
        int otpNum = otpService.generateOTP(key);

        EmailTemplate template = new EmailTemplate(templateFile);
        Map<String,String> replacements = new HashMap<>();
        replacements.put("user", email);
        replacements.put("otpNum", String.valueOf(otpNum));
        replacements.put("encodedKey", key);

        return template.getTemplate(replacements);
    }

    @Async
    public void sendOtpMail(String template, String recipient, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setHeader("Content-Type", "text/html;charset=\"utf-8\"");
        mimeMessage.setContent(template, "text/html;charset=utf-8");

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessageHelper.setFrom(SENDER_ADDRESS);
        mimeMessageHelper.setTo(recipient);
        mimeMessageHelper.setSubject(subject);

//        if (emailData.hasAttachments()) {
//            // preferring for loop here so we don't have to catch exceptions twice
//            for (EmailAttachment attachment : emailData.getAttachments()) {
//                mimeMessageHelper.addAttachment(attachment.getFilename(),
//                        attachment.getData(), attachment.getContentType());
//            }
//        }
        try{
            mailSender.send(mimeMessage);
            logger.info("mail send successfully");
        } catch (Throwable t) {
            if (t.getMessage().contains(EMAIL_ADDRESS_IS_NOT_VERIFIED)) {
                logger.error("mail SMTP failed.", t);
                throw new AppException(HttpStatus.FORBIDDEN, "EX40001", Arrays.asList("Mail address not verified"));
            }
            try {
                var err = String.format(
                        "ERROR_Send_mail_failed. subject:%s, to:%s, content:%s",
                        mimeMessage.getSubject(),
                        mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString(),
                        mimeMessage.getContent()
                );

                logger.error(err, t);
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "", Arrays.asList(err));
            }
            catch (IOException e) {
                logger.error("mail IOException", e);
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "", Arrays.asList("send mail failed"));
            }
        }
    }
}
