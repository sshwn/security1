package com.cos.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder {
}
