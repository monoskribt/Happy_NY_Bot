package com.example.telegram_bot_days_to_new_year.model;

import com.example.telegram_bot_days_to_new_year.enums.UserSubsStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bot_users")
public class BotUser {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'SUBSCRIBE'")
    private UserSubsStatus subscriptionStatus;

    public BotUser(Long id) {
        this.id = id;
        this.subscriptionStatus = UserSubsStatus.SUBSCRIBE;
    }

    public BotUser() {
        this.subscriptionStatus = UserSubsStatus.SUBSCRIBE;
    }

    public void subscribe() {
        this.subscriptionStatus = UserSubsStatus.SUBSCRIBE;
    }

    public void unsubscribe() {
        this.subscriptionStatus = UserSubsStatus.UNSUBSCRIBE;
    }

}