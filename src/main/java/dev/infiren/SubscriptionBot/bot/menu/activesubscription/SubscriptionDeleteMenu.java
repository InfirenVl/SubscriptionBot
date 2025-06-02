package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class SubscriptionDeleteMenu implements Menu {
    private final String MENU_TEXT = "Подписка удалена";
    private final MessageService messageService;
    private final CallbackDataPacker callbackDataPacker;
    private final UserRecordService userRecordService;

    public SubscriptionDeleteMenu(MessageService messageService, RecordService recordService, CallbackDataPacker callbackDataPacker, UserRecordService userRecordService) {
        this.messageService = messageService;
        this.callbackDataPacker = callbackDataPacker;
        this.userRecordService = userRecordService;
    }

    @Override
    public void show(Update update) {
        try {
            userRecordService.delete(update.getCallbackQuery().getMessage().getChatId(), Long.parseLong(callbackDataPacker.extractOne(update.getCallbackQuery().getData())));
            messageService.edit(update, MENU_TEXT, getKeyboard());
        } catch (RecordNotFoundException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ACTIVESUBSMENU.getCallback());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
