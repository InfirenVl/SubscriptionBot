package dev.infiren.SubscriptionBot.bot.menu.admin;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Request;
import dev.infiren.SubscriptionBot.service.RequestService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.format.DateTimeFormatter;

@Component
public class AdminRequestMenu implements Menu {
    private final MessageService messageService;
    private final CallbackDataPacker callbackDataPacker;
    private final RequestService requestService;

    public AdminRequestMenu(MessageService messageService, CallbackDataPacker callbackDataPacker, RequestService requestService) {
        this.messageService = messageService;
        this.callbackDataPacker = callbackDataPacker;
        this.requestService = requestService;
    }


    @Override
    public void show(Update update) {
        Request request = requestService.get(Integer.parseInt(callbackDataPacker.extractOne(update.getCallbackQuery().getData())));
        messageService.edit(update, getMessage(request), getKeyboard(request));
        if (!request.isViewed()) {
            request.setViewed(true);
            requestService.update(request);
        }
    }

    private InlineKeyboardMarkup getKeyboard(Request request) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton deleteButton = new InlineKeyboardButton("Удалить");
        deleteButton.setCallbackData(CallbackQuery.SHOW_ADMINREQUESTDELETEMENU.name() + callbackDataPacker.pack(request.getId()));
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADMINREQUESTLISTMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(deleteButton));
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }

    private String getMessage(Request request) {
        return String.format("От: %s \nСоздана: %s\n\n%s", request.getRequester().getName(), request.getCreationTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), request.getText());
    }
}
