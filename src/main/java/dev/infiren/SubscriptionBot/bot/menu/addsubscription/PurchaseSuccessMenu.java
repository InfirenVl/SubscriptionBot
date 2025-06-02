package dev.infiren.SubscriptionBot.bot.menu.addsubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.SubscriptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class PurchaseSuccessMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final SubscriptionService subscriptionService;
    private final CallbackDataPacker callbackDataPacker;
    private final String MENU_TEXT = "Подписка успешно приобретена";

    public PurchaseSuccessMenu(MessageService messageService, RecordService recordService, SubscriptionService subscriptionService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.subscriptionService = subscriptionService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        int subscriptionId = Integer.parseInt(callbackDataPacker.extractOne(update.getCallbackQuery().getData()));
        Subscription subscription;
        try {
            subscription = subscriptionService.getSubscription(subscriptionId);
        } catch (RecordNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            recordService.create(update.getCallbackQuery().getMessage().getChatId(), subscription);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        messageService.edit(update, MENU_TEXT, getKeyboard());
    }
    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("Вернуться");
        mainMenuButton.setCallbackData(CallbackQuery.SHOW_ADDSUBSMENU.name());
        InlineKeyboardRow row = new InlineKeyboardRow(mainMenuButton);
        builder.keyboardRow(row);
        return builder.build();
    }
}
