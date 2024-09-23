package com.example.tree_category_manager.telegram.commands;

/**
 * Interface for realising a design pattern 'Command' with method execute()
 * the implementation of which will change in implementing classes
 */
public interface Command {
    /**
     * Method with different implementation in implementation classes for doing their command specialisation
     * @param chatId
     * @param messageText
     */
    void execute(Long chatId, String messageText);
}
