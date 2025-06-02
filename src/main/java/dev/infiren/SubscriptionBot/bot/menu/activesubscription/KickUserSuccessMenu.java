package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class KickUserSuccessMenu implements Menu {
    private final String MENU_TEXT = "Пользователь успешно исключён";
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;
    private final MessageService messageService;

    public KickUserSuccessMenu(UserRecordService userRecordService, CallbackDataPacker callbackDataPacker, MessageService messageService) {
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
        this.messageService = messageService;
    }

    @Override
    public void show(Update update) {
        try {
            List<String> data = callbackDataPacker.extractAll(update.getCallbackQuery().getData());
            userRecordService.delete(Long.parseLong(data.get(0)), Long.parseLong(data.get(1)));
            messageService.edit(update, MENU_TEXT, getKeyboard(data.get(1)));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RecordNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private InlineKeyboardMarkup getKeyboard(String recordId) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_KICKUSERMENU.name() + callbackDataPacker.pack(recordId));
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
