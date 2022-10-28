package com.narko.ServitorFOS.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class ServitorConfig {
    @Value("${servitor.name}")
    String servitorName;
    @Value("${servitor.token}")
    String token;

}
