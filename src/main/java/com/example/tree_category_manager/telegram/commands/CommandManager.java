package com.example.tree_category_manager.telegram.commands;

import com.example.tree_category_manager.telegram.commands.commandImpl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Class invoker in design pattern Command 'Invoker' which using for calling a needed command by using method dispatchCommand(String commandName, Long chatId, String messageText)
 */
@Service
public class CommandManager {
    private final Map<String, Command> commandMap = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(CommandManager.class);

    /**
     * Including commands into Map&lt;String, Command&gt; commandMap the key of which is a command name and value is a Command object
     * @param startCommand
     * @param showCategoryCommand
     * @param createCategoryCommand
     * @param deleteCategoryCommand
     * @param addSubcategoryForCategoryCommand
     * @param getCategoryCommand
     * @param updateCategoryCommand
     * @param gettingChildCategoryCommand
     * @param getParentCommand
     */
    @Autowired
    public CommandManager(StartCommand startCommand, ShowCategoryCommand showCategoryCommand,
                          CreateCategoryCommand createCategoryCommand, DeleteCategoryCommand deleteCategoryCommand,
                          AddSubcategoryForCategoryCommand addSubcategoryForCategoryCommand, GetCategoryCommand getCategoryCommand,
                          UpdateCategoryCommand updateCategoryCommand, GettingChildCategoryCommand gettingChildCategoryCommand,
                          GetParentCommand getParentCommand, MainMenuCommand mainMenuCommand) {
        commandMap.put("/start", startCommand);
        commandMap.put("/get-categories", showCategoryCommand);
        commandMap.put("/create-category", createCategoryCommand);
        commandMap.put("/delete-category", deleteCategoryCommand);
        commandMap.put("/add-child", addSubcategoryForCategoryCommand);
        commandMap.put("/get-category", getCategoryCommand);
        commandMap.put("/update-category", updateCategoryCommand);
        commandMap.put("/get-children-category", gettingChildCategoryCommand);
        commandMap.put("/get-parent-category", getParentCommand);
        commandMap.put("/main-menu", mainMenuCommand);


    }

    /**
     * method which looking for a command by param commandName and putting it into a variable with name command.
     * After that if class variable have this command we use a method {@link Command#execute(Long, String)} and put a method params into it, else we log with status error
     * @param commandName
     * @param chatId
     * @param messageText
     */
    public void dispatchCommand(String commandName, Long chatId, String messageText) {
        Command command = commandMap.get(commandName);
        if (command != null) {
            command.execute(chatId, messageText);
        } else {
            logger.error("Команда не найдена: "+ commandName);
        }
    }

}
