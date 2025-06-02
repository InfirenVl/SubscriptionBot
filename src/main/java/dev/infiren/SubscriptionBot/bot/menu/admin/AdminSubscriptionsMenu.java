package dev.infiren.SubscriptionBot.bot.menu.admin;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.RecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class AdminSubscriptionsMenu implements Menu {
    private final RecordService recordService;
    private final MessageService messageService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Все подписки";

    public AdminSubscriptionsMenu(RecordService recordService, MessageService messageService, CallbackDataPacker callbackDataPacker) {
        this.recordService = recordService;
        this.messageService = messageService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        List<Record> recordList = recordService.getAll();
        messageService.edit(update, MENU_TEXT, getKeyboard(recordList));

    }

    private InlineKeyboardMarkup getKeyboard(List<Record> recordList) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        for (int i = 0; i < recordList.size(); i++) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(recordList.get(i)));
            builder.keyboardRow(row);
        }

        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("В главное меню");
        mainMenuButton.setCallbackData(CallbackQuery.SHOW_ADMINMAINMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(mainMenuButton));
        return builder.build();
    }

    private InlineKeyboardButton getButton(Record record) {
        InlineKeyboardButton button = new InlineKeyboardButton(record.getSubscriptionType().getName() +"("+ record.getCreator().getName()+")   ");
        button.setCallbackData(CallbackQuery.SHOW_ADMINSUBDETAILSMENU.name() + callbackDataPacker.pack(record.getId()));
        return button;
    }
}
