package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class ChangeActiveMenu implements Menu {
    private final MessageService messageService;
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Статус успешно сменён";

    public ChangeActiveMenu(MessageService messageService, UserRecordService userRecordService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        List<String> callbackData = callbackDataPacker.extractAll(update.getCallbackQuery().getData());
        long userId = Long.parseLong(callbackData.getFirst());
        long recordId = Long.parseLong(callbackData.getLast());
        userRecordService.changeStatus(userId, recordId);
        messageService.edit(update, MENU_TEXT, getKeyboard(recordId));
    }

    private InlineKeyboardMarkup getKeyboard(long recordId) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("В главное меню");
        mainMenuButton.setCallbackData(CallbackQuery.SHOW_SUBSETTINGSMENU.getCallback() + callbackDataPacker.pack(recordId));
        InlineKeyboardRow row = new InlineKeyboardRow(mainMenuButton);
        builder.keyboardRow(row);
        return builder.build();
    }
}
