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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Class for updating category
 */
@Service
public class UpdateCategoryCommand implements Command {
    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public UpdateCategoryCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * Method which get param form {@link Update#getMessage()} in from "old category name- new category name"
     * split it and put it into {@link CategoryTreeService#updateCategory(String oldName, String newName)}
     * and send message to client about successfully update.
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {

        String[] names = messageText.split("-");
        String oldName = names[0];
        String newName = names[1];

        CategoryTree categoryTree = categoryTreeService.updateCategory(oldName, newName);
        //Initializing message for client
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("the category with name" + oldName +
                "was renamed to: " + categoryTree.getName());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }

    }
}
