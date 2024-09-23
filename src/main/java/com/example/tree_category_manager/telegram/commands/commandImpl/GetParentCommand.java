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
 * Class for get one parent class
 */
@Service
public class GetParentCommand implements Command {

    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public GetParentCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * Method which get messageText from {@link Update#getCallbackQuery()} if variable start with 'getting-parent'
     * we get a parent name of category which come as variable and make a button with parent name, and make another button with words "Получить родителя"
     * if client click on it, it'll come again from {@link Update#getCallbackQuery()} and method work's will be used again.
     * if comes category don't have parent we send message with that problem and give him choose to go start menu.
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        String[] array = messageText.split("_");
        String categoryNameForFinding = array[array.length-1];

        CategoryTree categoryTree = categoryTreeService.getCategoryParent(categoryNameForFinding);
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (categoryTree != null&& categoryTree.getParent()!=null) {

            message.setText("your category is:");

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            String categoryName = categoryTree.getParent().getName();

            InlineKeyboardButton categoryButton = new InlineKeyboardButton(categoryName);
            categoryButton.setCallbackData("getting-category_%s".formatted(categoryName));

            InlineKeyboardButton parentButton = new InlineKeyboardButton("Получить родителя");
            parentButton.setCallbackData("getting-parent_%s".formatted(categoryName));

            rows.add(Arrays.asList(categoryButton));
            rows.add(Arrays.asList(parentButton));

            markup.setKeyboard(rows);
            message.setReplyMarkup(markup);
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            message.setText("no have parent.\nPlease click a button to go on main menu.");

            button.setText("/start");
            button.setCallbackData("start");
            rows.add(List.of(button));
            inlineKeyboardMarkup.setKeyboard(rows);
            message.setReplyMarkup(inlineKeyboardMarkup);
        }

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }

    }
}
