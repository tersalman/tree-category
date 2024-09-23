package com.example.tree_category_manager.telegram.commands.commandImpl;

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
 *  Class for deleting category
 */
@Service
public class DeleteCategoryCommand implements Command {

    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public DeleteCategoryCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     *  Method which get messageText from {@link Update#getMessage()}
     *  and put it into {@link CategoryTreeService#deleteCategory(String name)}.
     *  After that send message to client about successful deleting.
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        categoryTreeService.deleteCategory(messageText);

        //Initializing message for client
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("your category with name " + messageText + " has been deleted.");

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }



    }
}
