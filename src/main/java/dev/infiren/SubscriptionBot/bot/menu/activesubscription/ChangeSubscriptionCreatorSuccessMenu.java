package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
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
public class ChangeSubscriptionCreatorSuccessMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;
    private final UserService userService;
    private final String MENU_TEXT = "Вы были удалены из подписки, а создатель успешно изменён";

    public ChangeSubscriptionCreatorSuccessMenu(MessageService messageService, RecordService recordService, UserRecordService userRecordService, CallbackDataPacker callbackDataPacker, UserService userService) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
        this.userService = userService;
    }

    @Override
    public void show(Update update) {
        List<String> callback = callbackDataPacker.extractAll(update.getCallbackQuery().getData());
        Long recordId = Long.valueOf(callback.getFirst());
        Long userCallbackId = Long.valueOf(callback.getLast());
        Record record = recordService.getById(recordId);
        User user = userService.get(userCallbackId);
        record.setCreator(user);
        recordService.update(record);
        userRecordService.delete(update.getCallbackQuery().getMessage().getChatId(),recordId);
        messageService.edit(update, MENU_TEXT, getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ACTIVESUBSMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
