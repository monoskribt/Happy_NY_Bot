package com.example.telegram_bot_days_to_new_year.bot;

import com.example.telegram_bot_days_to_new_year.props.TelegramBotProps;
import com.example.telegram_bot_days_to_new_year.model.BotUser;
import com.example.telegram_bot_days_to_new_year.service.BotUserService;
import com.example.telegram_bot_days_to_new_year.service.impl.TelegramBotAnswersImpl;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.telegram_bot_days_to_new_year.constants.BotCommands.*;


public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProps telegramBotProps;
    private final BotUserService botUserService;
    private final TelegramBotAnswersImpl telegramBotAnswers;

    private final ConcurrentHashMap<Long, LocalDateTime> blockedUser = new ConcurrentHashMap<>();


    public TelegramBot(DefaultBotOptions options,
                       TelegramBotProps telegramBotProps,
                       BotUserService botUserService,
                       TelegramBotAnswersImpl telegramBotAnswers) {
        super(options, telegramBotProps.getToken());
        this.telegramBotProps = telegramBotProps;
        this.botUserService = botUserService;
        this.telegramBotAnswers = telegramBotAnswers;
    }



    @Override
    public String getBotUsername() {
        return telegramBotProps.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String messageText = message.getText();
            Long chatId = message.getChatId();

            if (isUserBlocked(chatId)) {
                telegramBotAnswers.blockedUserMessage(chatId);
                return;
            }

            switch (messageText) {
                case START_BOT:
                    telegramBotAnswers.startAnswer(chatId);
                    break;
                case UNSUBSCRIBE:
                    telegramBotAnswers.unsubscribeAnswer(chatId);
                    break;
                case SUBSCRIBE:
                    telegramBotAnswers.subscribeAnswer(chatId);
                    break;
                case QUIT_FROM_BOT:
                    telegramBotAnswers.quitAnswer(chatId);
                    botUserService.deleteUser(chatId);
                    blockedUser.put(chatId, LocalDateTime.now().plusSeconds(10));
                    break;
                default:
                    telegramBotAnswers.defaultAnswer(chatId);
                    break;
            }
        }
    }


    private boolean isUserBlocked(Long id) {
        LocalDateTime stillBlocked = blockedUser.get(id);
        if (stillBlocked == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(stillBlocked)) {
            blockedUser.remove(id);
            return false;
        }
        return true;
    }

    @Scheduled(cron = "0 0 8 * * *")
    @SneakyThrows
    private void leftDaysToNewYear() {
        long daysTo = helperDaysToNewYear();
        String text = "Days left to New Year: *" + daysTo + "*";

        List<BotUser> usersId = botUserService.findAllUsersWithSubscribeStatus();
        usersId.forEach(user -> telegramBotAnswers.sendMessage(user.getId(), text));
    }

    private long helperDaysToNewYear() {
        LocalDate today = LocalDate.now();
        LocalDate nextNewYear = LocalDate.of(today.getYear() + 1, 1, 1);
        return ChronoUnit.DAYS.between(today, nextNewYear);
    }
}
