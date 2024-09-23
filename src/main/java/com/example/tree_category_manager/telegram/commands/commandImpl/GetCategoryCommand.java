package com.example.tree_category_manager.telegram.commands.commandImpl;

import com.example.tree_category_manager.entity.CategoryTree;
import com.example.tree_category_manager.service.CategoryTreeService;
import com.example.tree_category_manager.telegram.TelegramBot;
import com.example.tree_category_manager.telegram.commands.Command;
import com.example.tree_category_manager.telegram.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
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
 * Class for get one special classes
 */
@Service
public class GetCategoryCommand implements Command {

    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public GetCategoryCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * Method which get messageText from {@link Update#getMessage()} or {@link Update#getCallbackQuery()} if variable start with 'category' or first word in var is a name of category it'll be found.
     * Or else we send message that category not found.
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {

        String[] array = messageText.split("_");
        String categoryNameForFinding = array[array.length - 1];

        CategoryTree categoryTree = categoryTreeService.getCategoryTree(categoryNameForFinding);
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (array[0].equals("category") || array.length - 1 == 0) {
            if (categoryTree != null) {

                message.setText("your category is:");

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();

                String categoryName = categoryTree.getName();

                InlineKeyboardButton categoryButton = new InlineKeyboardButton(categoryName);
                categoryButton.setCallbackData("getting-category_%s".formatted(categoryName));

                InlineKeyboardButton parentButton = new InlineKeyboardButton("Получить родителя");
                parentButton.setCallbackData("getting-parent_%s".formatted(categoryName));

                rows.add(Arrays.asList(categoryButton));
                rows.add(Arrays.asList(parentButton));

                markup.setKeyboard(rows);
                message.setReplyMarkup(markup);
            } else {
                message.setText("can't find a category with that name");
            }
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                logger.error("Exception with name%s".formatted(e.toString()));
            }

        }
    }
}
