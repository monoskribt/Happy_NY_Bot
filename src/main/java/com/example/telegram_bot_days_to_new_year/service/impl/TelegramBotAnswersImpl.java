package com.example.telegram_bot_days_to_new_year.service.impl;

import com.example.telegram_bot_days_to_new_year.enums.UserSubsStatus;
import com.example.telegram_bot_days_to_new_year.service.AnswersInterface;
import com.example.telegram_bot_days_to_new_year.service.BotUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.example.telegram_bot_days_to_new_year.enums.TextAnswersFromBot.*;

@Service
public class TelegramBotAnswersImpl implements AnswersInterface {

    private final BotUserService botUserService;

    @Autowired
    @Lazy
    private AbsSender absSender;

    public TelegramBotAnswersImpl(BotUserService botUserService) {
        this.botUserService = botUserService;
    }

    @Override
    public void startAnswer(Long id) {
        if (botUserService.isUserExistsWithId(id)) {
            UserSubsStatus status = botUserService.getUserSubscriptionStatus(id);
            sendMessage(id, status.equals(UserSubsStatus.SUBSCRIBE) ?
                    SUBSCRIBED_ALREADY_TEXT.getText() : UNSUBSCRIBE_WITHOUT_MAIN_PATH_TEXT.getText());
        } else {
            sendMessage(id, START_TEXT.getText());
            botUserService.addUser(id);
        }
    }

    @Override
    public void unsubscribeAnswer(Long id) {
        botUserService.unsubscribeUser(id);
        sendMessage(id, UNSUBSCRIBE_TEXT.getText());
    }

    @Override
    public void subscribeAnswer(Long id) {
        botUserService.subscribeUser(id);
        sendMessage(id, SUBSCRIBE_TEXT.getText());
    }

    @Override
    public void quitAnswer(Long id) {
        sendMessage(id, QUIT_TEXT.getText());
    }

    @Override
    public void defaultAnswer(Long id) {
        sendMessage(id, DEFAULT_TEXT.getText());
    }

    @Override
    public void blockedUserMessage(Long id) {
        sendMessage(id, BLOCKED_USER_TEXT.getText());
    }

    @SneakyThrows
    public void sendMessage(Long id, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(id.toString())
                .parseMode("Markdown")
                .text(text)
                .build();
        absSender.execute(message);
    }
}
