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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for get subcategories
 */
@Service
public class GettingChildCategoryCommand implements Command {

    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public GettingChildCategoryCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * Method which get messageText from {@link Update#getCallbackQuery()} and writing in form: 'getting-category_className', method splits messageText on symbol '_' and get class subclasses after that method put it into {@link #getInlineKeyboardMarkup(List CategoryTree subclasses, String className)}
     * if category don't have subclasses we send message with that problem and give him choose to go start menu.
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        String[] ar = messageText.split("_");
        String name = null;
        if (!ar[0].contentEquals("start")) {
            name = ar[ar.length-1];
        }
        List<CategoryTree> allSubclasses = categoryTreeService.getAllSubcategories(name);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (!allSubclasses.isEmpty()) {
            message.setText("this is subclasses of your category");

            InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(allSubclasses, name);
            message.setReplyMarkup(inlineKeyboardMarkup);


        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            message.setText("There no category more.\nPlease click the button to go on /start menu.");

            button.setText("/start");
            button.setCallbackData("start");
            rows.add(List.of(button));
            inlineKeyboardMarkup.setKeyboard(rows);
            message.setReplyMarkup(inlineKeyboardMarkup);

        }
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name %s".formatted(e.toString()));
        }
    }

    /**
     * private method for creating buttons with categoryName and callback data "getting-category_categoryName"
     * and one button with words "Получить родителя" and callback data "getting-parent_categoryName".
     * @param categoryTrees
     * @param name
     * @return InlineKeyboardMarkup inlineKeyboardMarkup
     */
    private static InlineKeyboardMarkup getInlineKeyboardMarkup(List<CategoryTree> categoryTrees, String name) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardButton parentButton = new InlineKeyboardButton("Получить родителя");
        parentButton.setCallbackData("getting-parent_%s".formatted(name));


        for (CategoryTree category : categoryTrees) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            //set name for our button and the callBackData
            button.setText(category.getName());
            button.setCallbackData("getting-category_%s".formatted(category.getName()));
            // set the buttons into a row and set a row into a rows
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }
        rows.add(Arrays.asList(parentButton));
        markup.setKeyboard(rows);
        return markup;
    }
}
