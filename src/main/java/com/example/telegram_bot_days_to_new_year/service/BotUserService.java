package com.example.telegram_bot_days_to_new_year.service;

import com.example.telegram_bot_days_to_new_year.enums.UserSubsStatus;
import com.example.telegram_bot_days_to_new_year.model.BotUser;

import java.util.List;

public interface BotUserService {

    void addUser(Long id);
    void deleteUser(Long id);
    void subscribeUser(Long id);
    void unsubscribeUser(Long id);
    boolean isUserExistsWithId(Long id);
    List<BotUser> findAllUsersWithSubscribeStatus();
    UserSubsStatus getUserSubscriptionStatus(Long id);
}
