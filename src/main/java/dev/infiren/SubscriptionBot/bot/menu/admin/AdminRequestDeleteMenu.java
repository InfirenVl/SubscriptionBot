package dev.infiren.SubscriptionBot.bot.menu.admin;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.service.RequestService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class AdminRequestDeleteMenu implements Menu {
    private final MessageService messageService;
    private final RequestService requestService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Заявка удалена";

    public AdminRequestDeleteMenu(MessageService messageService, RequestService requestService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.requestService = requestService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        messageService.edit(update, MENU_TEXT, getKeyboard());
        requestService.delete(Integer.parseInt(callbackDataPacker.extractOne(update.getCallbackQuery().getData())));
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADMINREQUESTLISTMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
