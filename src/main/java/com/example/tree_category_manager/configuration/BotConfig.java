package com.example.tree_category_manager.configuration;

import com.example.tree_category_manager.telegram.TelegramBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
//    @Value("${telegram.bot.username}")
//    private String botUsername;
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
//    public BotConfig(String botUsername, String botToken) {
//        this.botUsername = botUsername;
//        this.botToken = botToken;
//    }
////
//    @Bean
//    public TelegramBot categoryBot() {
//        TelegramBot bot = new TelegramBot(botUsername, botToken);
//        TelegramBotsApi telegramBotsApi;
//        try {
//            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(bot); // Регистрация бота
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//        return bot;
//    }

//    public String getBotUsername() {
//        return botUsername;
//    }

//    public String getBotToken() {
//        return botToken;
//    }
}
