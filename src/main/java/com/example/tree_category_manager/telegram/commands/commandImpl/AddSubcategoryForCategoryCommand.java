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
 * Class for adding child for category
 */
@Service
public class AddSubcategoryForCategoryCommand implements Command {
    private TelegramBot bot;
    private CategoryTreeService categoryTreeService;
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public AddSubcategoryForCategoryCommand(@Lazy TelegramBot bot, CategoryTreeService categoryTreeService) {
        this.bot = bot;
        this.categoryTreeService = categoryTreeService;
    }

    /**
     * Method which get messageText from {@link Update#getMessage()} and it must be written in form (parent name-subcategory name).
     * After that we look on messageText if it has "-" we're splitting it on array 'names' and put names into {@link CategoryTreeService#addSubCategory(String mainCategoryName, String subcategoryName)} after that we send message to client about successful end.
     * Else we send message were say about right form for writing names.
     * @param chatId
     * @param messageText
     */
    @Override
    public void execute(Long chatId, String messageText) {
        //Initializing message for client
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        //Check for contains '-' in messageText
        if (messageText.contains("-")) {
            //Split messageText and put it into addSubclasses()
            String[] names = messageText.split("-");
            CategoryTree categoryTree = categoryTreeService.addSubCategory(names[0].trim(), names[1].trim());

            message.setText("the child category with name" + categoryTree.getName() +
                    "was added to parent category" + categoryTree.getParent());
        } else {
            message.setText("Please write a message in right form (parent name-subclass name)");
        }
        // send message for client
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }

    }
}
