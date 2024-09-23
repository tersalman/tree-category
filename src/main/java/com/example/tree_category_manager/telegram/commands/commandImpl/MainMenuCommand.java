package com.example.tree_category_manager.telegram.commands.commandImpl;

import com.example.tree_category_manager.service.CategoryTreeService;
import com.example.tree_category_manager.telegram.TelegramBot;
import com.example.tree_category_manager.telegram.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for main menu command
 */
@Service
public class MainMenuCommand implements Command {
    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public MainMenuCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * method for creating buttons with word start for go to start menu
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        button.setText("/start");
        button.setCallbackData("start");
        message.setText(messageText);
        rows.add(List.of(button));
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name %s".formatted(e.toString()));
        }
    }
}
