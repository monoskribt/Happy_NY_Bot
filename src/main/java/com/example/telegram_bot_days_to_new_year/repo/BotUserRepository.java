package com.example.telegram_bot_days_to_new_year.repo;

import com.example.telegram_bot_days_to_new_year.model.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
}
