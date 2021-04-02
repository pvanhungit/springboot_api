package com.awesome.service.user;

import com.awesome.controller.dto.UserInsertDTO;
import com.awesome.domain.User;
import com.awesome.exception.AppException;
import com.awesome.repository.UserRepository;
import com.awesome.service.utility.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    @Lazy
    EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndIsEnabled(phoneNumber, true);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndIsEnabled(email, true);
    }

    @Override
    @Transactional
    public User createUser(UserInsertDTO dto) {
        String phone = dto.getPhoneNumber() == null ? "" : dto.getPhoneNumber();
        String email = dto.getEmail() == null ? "" : dto.getEmail();

        if(phone.isBlank() && email.isBlank())
            throw new AppException(HttpStatus.BAD_REQUEST, "Bad request", Arrays.asList());

        HashMap<String, String> checkValues = new HashMap<>();
        checkValues.put("phone", phone);
        checkValues.put("email", email);

        if( !checkExistUser(checkValues) ) {
            User user = new User();
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setEmail(dto.getEmail());
//            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setIsEnabled(false);
            user.setCreatedBy(1);
            user.setUserKeyCloakId(dto.getUserKeyCloakId());
            userRepository.save(user);
            try{

                String template = emailService.generateUserVerifyMailTemplate(
                        "templates/verify_email.html", user.getEmail());
                // send mail
                try {
                    emailService.sendOtpMail(template, user.getEmail(), "Your secret key to login into futbol");
                    logger.info("Email sent successfully.");
                } catch (MessagingException e) {
                    logger.error( String.format("Failed to send email due to %s", e.getMessage()) );
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }

            return user;
        }
        return null;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean checkExistUser(Map<String,String> valuesToCheck) {
        String phone = valuesToCheck.get("phone");
        String email = valuesToCheck.get("email");
        Optional<User> user;

        if(!phone.isBlank()){
            user = userRepository.findByPhoneNumber(phone);
        }
        else {
            user = userRepository.findByEmail(email);
        }

        return user.isPresent();
    }

    public int enableUser(Long userId){
        findById(userId).ifPresent(
                user ->  user.setIsEnabled(true));
        return 1;
    }
}
