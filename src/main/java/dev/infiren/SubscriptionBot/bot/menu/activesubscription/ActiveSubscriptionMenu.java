package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;
@Component
public class ActiveSubscriptionMenu implements Menu {
    private final String MENU_TEXT = "Активные подписки";
    private final MessageService messageService;
    private final UserRecordService userRecordService;

    public ActiveSubscriptionMenu(MessageService messageService, UserRecordService userRecordService) {
        this.messageService = messageService;
        this.userRecordService = userRecordService;
    }

    @Override
    public void show(Update update) {
        messageService.edit(update, MENU_TEXT, getKeyboard(update.getCallbackQuery().getMessage().getChatId()));
    }
    private InlineKeyboardMarkup getKeyboard(long userId) {
        List<Record> recordList;
        try {
            recordList = userRecordService.getAllRecordsByUser(userId);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        for (int i = 0; i <= recordList.size() - 2; i += 2) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(recordList.get(i)), getButton(recordList.get(i+1)));
            builder.keyboardRow(row);
        }
        if(recordList.size() % 2 != 0) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(recordList.getLast()));
            builder.keyboardRow(row);
        }
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("В главное меню");
        mainMenuButton.setCallbackData(CallbackQuery.SHOW_MAINMENU.getCallback());
        InlineKeyboardRow row = new InlineKeyboardRow(mainMenuButton);
        builder.keyboardRow(row);
        return builder.build();
    }
    private InlineKeyboardButton getButton(Record record) {
        InlineKeyboardButton button = new InlineKeyboardButton(record.getSubscriptionType().getName());
        button.setCallbackData(CallbackQuery.SHOW_SUBSETTINGSMENU.getCallback() + CallbackQuery.DATA.getCallback() + record.getId());
        return button;
    }
}
