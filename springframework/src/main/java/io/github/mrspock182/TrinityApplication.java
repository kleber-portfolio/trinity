package io.github.mrspock182;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrinityApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrinityApplication.class);

    @Value("${app.version}")
    private String appVersion;

    public static void main(String[] args) {
        SpringApplication.run(TrinityApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LOGGER.info("Versão da aplicação: {}", appVersion);
    }
}
