package com.example.tree_category_manager.telegram.commands.commandImpl;

import com.example.tree_category_manager.telegram.TelegramBot;
import com.example.tree_category_manager.telegram.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for start action with main menu in the form of buttons
 */
@Service
public class StartCommand implements Command {

    private final TelegramBot bot;
    private Logger logger = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand(@Lazy TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Method for initializing a menu with buttons for all commands
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        //Initializing message for client
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(messageText);
        //Initializing markup for buttons
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        // Initializing buttons for creating and set name and callbackData (2 button in one row)
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("Создать категорию");
        inlineKeyboardButton1.setCallbackData("create_category");
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton("Добавить подкласс");
        inlineKeyboardButton5.setCallbackData("add-child_category");
        rows.add(Arrays.asList(inlineKeyboardButton1, inlineKeyboardButton5));
        //Initializing buttons for getting and set name and callbackData (2 button in one row)
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("Просмотреть категории");
        inlineKeyboardButton2.setCallbackData("view_categories");
        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton("Просмотреть категорию");
        inlineKeyboardButton6.setCallbackData("get_category");
        rows.add(Arrays.asList(inlineKeyboardButton2, inlineKeyboardButton6));
        //Initializing button for deleting and set name and callbackData (1 button in one row)
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton("Удалить категорию");
        inlineKeyboardButton3.setCallbackData("delete_category");
        rows.add(Arrays.asList(inlineKeyboardButton3));
        //Initializing button for editing and set name and callbackData (1 button in one row)
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton("Изменить категорию");
        inlineKeyboardButton4.setCallbackData("edit_category");
        rows.add(Arrays.asList(inlineKeyboardButton4));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);
        // send message for client
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }
    }


}
