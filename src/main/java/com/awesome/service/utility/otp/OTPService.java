package com.awesome.service.utility.otp;

import com.awesome.common.utils.CipherUtil;

import com.awesome.service.user.UserServiceImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Integer EXPIRE_MIN = 5;

    private LoadingCache<String, Integer> otpCache;

    @Autowired
    @Lazy
    private UserServiceImpl userService;

    public OTPService(){
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    public Integer load(@ParametersAreNonnullByDefault String key) {
                        return 0;
                    }
        });
    }

    //This method is used to push the opt number against Key. using user email as key
    public int generateOTP(String key){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    //This method is used to return the OPT number against Key->Key values is email
    public int getOtp(String key){
        try{
            return otpCache.get(key);
        }
        catch (Exception e){
            return 0;
        }
    }

    //Validate the Otp
    public Boolean validateOtp(OTPRequest otpRequest) {
        var key = otpRequest.getKey();
        var otpNum = otpRequest.getCode();

        if(otpNum > 0) {
            int serverOtp = getOtp(key);

            if (serverOtp > 0 && otpNum == serverOtp) {
                String userId = "";
                try {
                    userId = CipherUtil.decode(key);
                    userService.enableUser(Long.valueOf(userId));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                clearOTP(userId);
                return true;
            }
        }
        return false;
    }

    //This method is used to clear the OTP cached already
    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}
