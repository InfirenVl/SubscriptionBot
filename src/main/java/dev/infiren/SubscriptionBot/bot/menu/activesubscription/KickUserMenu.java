package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class KickUserMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final UserService userService;
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Выберите пользователя которого хотите исключить";

    public KickUserMenu(MessageService messageService, RecordService recordService, UserService userService, UserRecordService userRecordService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.userService = userService;
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        try {
            long recordId = Long.parseLong(callbackDataPacker.extractOne(update.getCallbackQuery().getData()));
            List<User> users = userRecordService.getAllUsersByRecord(recordId);
            users.remove(userService.get(update.getCallbackQuery().getMessage().getChatId()));
            messageService.edit(update,MENU_TEXT, getKeyboard(users, recordId));
        } catch (RecordNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private InlineKeyboardMarkup getKeyboard(List<User> userList, long recordId) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        for (int i = 0; i < userList.size(); i ++) {
            builder.keyboardRow(new InlineKeyboardRow(getButton(userList.get(i), recordId)));
        }
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("В главное меню");
        mainMenuButton.setCallbackData(CallbackQuery.SHOW_SUBSETTINGSMENU.getCallback() + CallbackQuery.DATA.getCallback() + recordId);
        InlineKeyboardRow row = new InlineKeyboardRow(mainMenuButton);
        builder.keyboardRow(row);
        return builder.build();
    }

    private InlineKeyboardButton getButton(User user, long recordId) {
        InlineKeyboardButton button = new InlineKeyboardButton(user.getName());
        button.setCallbackData(CallbackQuery.SHOW_KICKUSERSUCCESSMENU.getCallback() + callbackDataPacker.pack(user.getId(), recordId));
        return button;
    }
}
