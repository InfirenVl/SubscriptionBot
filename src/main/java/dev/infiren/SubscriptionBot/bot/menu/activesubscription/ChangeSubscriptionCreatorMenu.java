package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class ChangeSubscriptionCreatorMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Выберите нового создателя подписки";

    public ChangeSubscriptionCreatorMenu(MessageService messageService, RecordService recordService, UserRecordService userRecordService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        long recordId = Long.parseLong(callbackDataPacker.extractOne(update.getCallbackQuery().getData()));
        messageService.edit(update, MENU_TEXT, getKeyboard(recordId, update.getCallbackQuery().getMessage().getChatId()));
    }

    private InlineKeyboardMarkup getKeyboard(long recordId, long userId) {
        List<User> users = userRecordService.getAllUsersByRecord(recordId);
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        for (User user : users) {
            if(user.getId() == userId)
                continue;
            InlineKeyboardButton userButton = new InlineKeyboardButton(user.getName());
            userButton.setCallbackData(CallbackQuery.SHOW_CHANGESUBCREATORSUCCESSMENU.name() + callbackDataPacker.pack(recordId, user.getId()));
            builder.keyboardRow(new InlineKeyboardRow(userButton));
        }
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_SUBSETTINGSMENU.name() + callbackDataPacker.pack(recordId));
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
