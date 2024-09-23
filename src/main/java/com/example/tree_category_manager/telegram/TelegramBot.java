package com.example.tree_category_manager.telegram;

import com.example.tree_category_manager.configuration.BotConfig;
import com.example.tree_category_manager.telegram.commands.CommandManager;
import com.example.tree_category_manager.telegram.constants.Constants;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tree_category_manager.telegram.constants.Constants.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;
    private CommandManager commandManager;
    private final Map<Long, Constants> chatStatus = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Autowired
    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Constructor for configure bot with botToken and botUserName.
     *
     * @param botToken    telegram bot token from BotFather
     * @param botUsername name of bot and name of postgreSQL dataBase
     */
    public TelegramBot(@Value("${telegram.bot.token}") String botToken, @Value("${spring.datasource.username}") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    /**
     * Method for register bot. No configuration class were used.
     *
     * @throws TelegramApiException
     */
    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        if (!chatStatus.containsKey(chatId)) {
            chatStatus.put(chatId, COMMON);
        }
        if (update.hasMessage() && update.getMessage().hasText() && chatStatus.get(chatId).equals(COMMON)) {
            commandManager.dispatchCommand("/main-menu", chatId, "Welcome on category tree manager.please click on /start");
        } else if (chatStatus.get(chatId).equals(WAITING_FOR_CATEGORY_NAME)) {
            commandManager.dispatchCommand("/create-category", chatId, update.getMessage().getText());
            setStatusForChat(chatId, COMMON, "need another ");
        } else if (chatStatus.get(chatId).equals(WAITING_FOR_DELETE_CATEGORY)) {
            commandManager.dispatchCommand("/delete-category", chatId, update.getMessage().getText());
            setStatusForChat(chatId, COMMON, "deleting has been completed");
        } else if (chatStatus.get(chatId).equals(WAITING_FOR_ADDING_CHILD_FOR_CATEGORY)) {
            commandManager.dispatchCommand("/add-child", chatId, update.getMessage().getText());
            setStatusForChat(chatId, COMMON, "adding has been completed");
        } else if (chatStatus.get(chatId).equals(WAITING_FOR_UPDATE)) {
            commandManager.dispatchCommand("/update-category", chatId, update.getMessage().getText());
            setStatusForChat(chatId, COMMON, "updating has been completed");
        } else if (chatStatus.get(chatId).equals(WAITING_FOR_WRITING_NAME_TO_GET_CATEGORY)) {
            commandManager.dispatchCommand("/get-category", chatId, update.getMessage().getText());
            setStatusForChat(chatId, COMMON, "updating has been completed");
        }
        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            switch (callBackData) {
                case "view_categories" -> {
                    sendMessage(chatId, "please choose wanted category");
                    commandManager.dispatchCommand("/get-categories", chatId, null);
                }
                case "create_category" ->
                        setStatusForChat(chatId, WAITING_FOR_CATEGORY_NAME, "enter your category name");
                case "delete_category" -> setStatusForChat(chatId, WAITING_FOR_DELETE_CATEGORY,
                        "Please enter category name which you need delete");
                case "add-child_category" -> setStatusForChat(chatId, WAITING_FOR_ADDING_CHILD_FOR_CATEGORY,
                        "Please enter parent name and child name in this pattern\\n 'parent name-child name'");
                case "edit_category" -> setStatusForChat(chatId, WAITING_FOR_UPDATE,
                        "Please enter category old name and new name as this example 'old name-new name'");
                case "get_category" -> setStatusForChat(chatId, WAITING_FOR_WRITING_NAME_TO_GET_CATEGORY,
                        "Please write  category name which you need to get");
                case "start" -> {
                    sendMessage(chatId, "you are on start menu");
                    commandManager.dispatchCommand("/start", chatId, "Выберите действие:");
                }
                default -> {
                    if (callBackData.startsWith("getting-category")) {
                        commandManager.dispatchCommand("/get-children-category", chatId, callBackData);
                        setStatusForChat(chatId, COMMON, "This is your subclasses");
                    } else if (callBackData.startsWith("getting-parent")) {
                        commandManager.dispatchCommand("/get-parent-category", chatId, callBackData);
                        setStatusForChat(chatId, COMMON, "This is your parent class");
                    } else if (callBackData.startsWith("category")) {
                        commandManager.dispatchCommand("/get-category", chatId, callBackData);
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Exception with name%s".formatted(e.toString()));
        }
    }

    public void setStatusForChat(Long chatId, Constants status, String message) {
        chatStatus.put(chatId, status);
        sendMessage(chatId, message);
        logger.info("chat with id %s will set on status %s".formatted(chatId, status));
    }

}
