package com.awesome.config;

import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public DozerBeanMapper dozerMapper(){
        var source = (DozerBeanMapper)DozerBeanMapperSingletonWrapper.getInstance();
        return  source;
    }
}
