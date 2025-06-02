package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.service.RecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class RegenerateCodeMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Код успешно обновлён";

    public RegenerateCodeMenu(MessageService messageService, RecordService recordService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        long recordId = Long.parseLong(callbackDataPacker.extractOne(update.getCallbackQuery().getData()));
        try {
            recordService.updateCode(recordId);
        } catch (RecordNotFoundException e) {
            throw new RuntimeException(e);
        }
        messageService.edit(update, MENU_TEXT, getKeyboard(recordId));
    }

    private InlineKeyboardMarkup getKeyboard(long recordId) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_SUBSETTINGSMENU.name() + CallbackQuery.DATA.getCallback() + recordId);
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
