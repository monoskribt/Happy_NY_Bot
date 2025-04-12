package com.example.telegram_bot_days_to_new_year.service.impl;

import com.example.telegram_bot_days_to_new_year.enums.UserSubsStatus;
import com.example.telegram_bot_days_to_new_year.model.BotUser;
import com.example.telegram_bot_days_to_new_year.repo.BotUserRepository;
import com.example.telegram_bot_days_to_new_year.service.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {

    private final BotUserRepository botUserRepository;

    @Override
    public void addUser(Long id) {
        if(!botUserRepository.existsById(id)) {
            botUserRepository.save(new BotUser(id));
        }
    }

    @Override
    public void deleteUser(Long id) {
        botUserRepository.findById(id).ifPresent(botUserRepository::delete);
    }

    @Override
    public void subscribeUser(Long id) {
        botUserRepository.findById(id)
                .ifPresent(user -> {
                    user.subscribe();
                    botUserRepository.save(user);
                });
    }

    @Override
    public void unsubscribeUser(Long id) {
        botUserRepository.findById(id)
                .ifPresent(user -> {
                    user.unsubscribe();
                    botUserRepository.save(user);
                });
    }

    @Override
    public boolean isUserExistsWithId(Long id) {
        return botUserRepository.existsById(id);
    }

    @Override
    public List<BotUser> findAllUsersWithSubscribeStatus() {
        return botUserRepository.findAll().stream()
                .filter(user -> user.getSubscriptionStatus().equals(UserSubsStatus.SUBSCRIBE))
                .collect(Collectors.toList());
    }

    @Override
    public UserSubsStatus getUserSubscriptionStatus(Long id) {
        return botUserRepository.findById(id)
                .map(BotUser::getSubscriptionStatus)
                .orElse(null);
    }
}
