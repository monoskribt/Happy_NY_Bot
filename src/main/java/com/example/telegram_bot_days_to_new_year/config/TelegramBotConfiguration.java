package com.example.telegram_bot_days_to_new_year.config;

import com.example.telegram_bot_days_to_new_year.bot.TelegramBot;
import com.example.telegram_bot_days_to_new_year.props.TelegramBotProps;
import com.example.telegram_bot_days_to_new_year.service.BotUserService;
import com.example.telegram_bot_days_to_new_year.service.impl.TelegramBotAnswersImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfiguration {

    private final TelegramBotProps telegramBotProps;
    private final BotUserService botUserService;
    private final TelegramBotAnswersImpl telegramBotAnswers;


    @Bean
    @SneakyThrows
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    @SneakyThrows
    public TelegramBot telegramBot(TelegramBotsApi telegramBotsApi) {
        var botOptions = new DefaultBotOptions();
        var bot = new TelegramBot(botOptions, telegramBotProps, botUserService, telegramBotAnswers);
        telegramBotsApi.registerBot(bot);
        return bot;
    }
}
