package com.example.telegram_bot_days_to_new_year.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class TelegramBotConfig {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;
}
