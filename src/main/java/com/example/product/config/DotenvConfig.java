package com.example.product.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Устанавливаем переменные окружения для приложения
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue())
        );
    }
}
