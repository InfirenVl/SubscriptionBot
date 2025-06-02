package dev.infiren.SubscriptionBot.bot.menu;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class MainMenu implements Menu {
    private final MessageService messageService;
    private final UserService userService;
    private final String MENU_TEXT = "Главное меню";
    private final String adminName;

    public MainMenu(MessageService messageService, UserService userService, @Value("${bot.admin}") String adminName) {
        this.messageService = messageService;
        this.userService = userService;
        this.adminName = adminName;
    }

    @Override
    public void show(Update update) {
        if(update.hasCallbackQuery()){
            try {
                User user = userService.get(update.getCallbackQuery().getMessage().getChatId());
                messageService.edit(update, MENU_TEXT, getKeyboard(user));
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                User user = userService.get(update.getMessage().getChatId());
                messageService.send(update.getMessage().getChatId(), MENU_TEXT, getKeyboard(user));
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private InlineKeyboardMarkup getKeyboard(User user) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton activeSubButton = new InlineKeyboardButton("Активные подписки");
        activeSubButton.setCallbackData(CallbackQuery.SHOW_ACTIVESUBSMENU.getCallback());
        InlineKeyboardButton addSubButton = new InlineKeyboardButton("Добавить подписку");
        addSubButton.setCallbackData(CallbackQuery.SHOW_ADDSUBSMENU.getCallback());
        InlineKeyboardRow row1 = new InlineKeyboardRow(activeSubButton, addSubButton);
        builder.keyboardRow(row1);
        if(user.getName().equals(adminName)){
            InlineKeyboardButton adminMenuButton = new InlineKeyboardButton("Панель администратора");
            adminMenuButton.setCallbackData(CallbackQuery.SHOW_ADMINMAINMENU.getCallback());
            builder.keyboardRow(new InlineKeyboardRow(adminMenuButton));
        }
        return builder.build();
    }
}
