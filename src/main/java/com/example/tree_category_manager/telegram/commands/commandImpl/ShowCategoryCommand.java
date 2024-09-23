package com.example.tree_category_manager.telegram.commands.commandImpl;

import com.example.tree_category_manager.entity.CategoryTree;
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
import java.util.Arrays;
import java.util.List;

/**
 * Class for show all root categories
 */
@Service
public class ShowCategoryCommand implements Command {
    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public ShowCategoryCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * Method which show all root categories and put it into {@link #getInlineKeyboardMarkup(List CategoryTree categoryTree)}
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        List<CategoryTree> categoryTrees = categoryTreeService.getRootCategories();
        //Initializing message for client
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("this is your categories");

        InlineKeyboardMarkup markup = getInlineKeyboardMarkup(categoryTrees);
        message.setReplyMarkup(markup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }

    }

    /**
     * private method for creating buttons with categoryName and callback data "category_categoryName"
     * @param categoryTrees
     * @return InlineKeyboardMarkup markup
     */
    private static InlineKeyboardMarkup getInlineKeyboardMarkup(List<CategoryTree> categoryTrees) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (CategoryTree category : categoryTrees) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            //set name for our button and the callBackData
            button.setText(category.getName());
            button.setCallbackData("category_%s".formatted(category.getName()));
            // set the buttons into a row and set a row into a rows
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }
        markup.setKeyboard(rows);
        return markup;
    }
}
