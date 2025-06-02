package dev.infiren.SubscriptionBot.bot.menu.addsubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class UseCodeMenu implements Menu {
    private final MessageService messageService;
    private final UserService userService;
    private final String MENU_TEXT = "Введите код приглашения. Перед отправлением следует убедиться в отсутствии лишних символов";

    public UseCodeMenu(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void show(Update update) {
        long id = -1;
        if(update.hasMessage())
            id = update.getMessage().getChatId();
        if (update.hasCallbackQuery())
            id = update.getCallbackQuery().getMessage().getChatId();
        try {
            userService.updateStatement(id, User.Statement.ENTERING_CODE);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        messageService.edit(update, MENU_TEXT, getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADDSUBSMENU.getCallback() + CallbackQuery.RESET_STATEMENTANDMETADATA.getCallback());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
