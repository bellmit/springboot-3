package com.leo.boot.jpa.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@ConditionalOnClass(JpaRepository.class)
@Configuration
public class JPAConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}
