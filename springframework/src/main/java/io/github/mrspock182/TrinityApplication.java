package io.github.mrspock182;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrinityApplication implements CommandLineRunner {
    @Value("${app.version}")
    private String appVersion;

    public static void main(String[] args) {
        SpringApplication.run(TrinityApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Versão da aplicação: " + appVersion);
    }
}
