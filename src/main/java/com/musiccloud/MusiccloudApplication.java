package com.musiccloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class MusiccloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusiccloudApplication.class, args);
    }

}
