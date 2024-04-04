package com.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MyProjectBackendApplicationTests {

    @Resource
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        System.out.println(this.passwordEncoder.encode("123456"));
    }
}
