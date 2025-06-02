package dev.infiren.SubscriptionBot.bot.menu.admin;

import com.vdurmont.emoji.EmojiParser;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Request;
import dev.infiren.SubscriptionBot.service.RequestService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class AdminRequestListMenu implements Menu {
    private final MessageService messageService;
    private final RequestService requestService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Оставленные заявки";

    public AdminRequestListMenu(MessageService messageService, RequestService requestService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.requestService = requestService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        List<Request> requests = requestService.getAll();
        messageService.edit(update, MENU_TEXT, getKeyboard(requests));
    }

    private InlineKeyboardMarkup getKeyboard(List<Request> requests) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        for (int i = 0; i <= requests.size() - 2; i += 2) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(requests.get(i)), getButton(requests.get(i+1)));
            builder.keyboardRow(row);
        }
        if(requests.size() % 2 != 0) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(requests.getLast()));
            builder.keyboardRow(row);
        }
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADMINMAINMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
    private InlineKeyboardButton getButton(Request request) {
        String status = "";
        if (request.isViewed())
            status = EmojiParser.parseToUnicode(":eye:");
        InlineKeyboardButton button = new InlineKeyboardButton(request.getRequester().getName() + status);
        button.setCallbackData(CallbackQuery.SHOW_ADMINREQUESTMENU.name() + callbackDataPacker.pack(request.getId()));
        return button;
    }
}
