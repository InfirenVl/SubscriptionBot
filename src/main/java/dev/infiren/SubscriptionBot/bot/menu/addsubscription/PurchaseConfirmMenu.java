package dev.infiren.SubscriptionBot.bot.menu.addsubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.service.SubscriptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class PurchaseConfirmMenu implements Menu {
    private final SubscriptionService subscriptionService;
    private final MessageService messageService;
    private final CallbackDataPacker callbackDataPacker;

    public PurchaseConfirmMenu(SubscriptionService subscriptionService, MessageService messageService, CallbackDataPacker callbackDataPacker) {
        this.subscriptionService = subscriptionService;
        this.messageService = messageService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        Subscription subscription;
        try {
            subscription = getSubscription(update);
        } catch (RecordNotFoundException e) {
            return;
        }
        System.out.println(subscription);
        messageService.edit(update, getMessage(subscription), getKeyboard(subscription.getId()));
    }

    private String getMessage(Subscription subscription) {
        return String.format("Вы уверены что хотите приобрести подписку: \n%s\nСтоимость: %d₽\nМаксимальное число участников: %d",
                subscription.getName(),
                subscription.getPrice(),
                subscription.getMaxMembers());
    }
    private Subscription getSubscription(Update update) throws RecordNotFoundException {
        int subscriptionId = Integer.parseInt(callbackDataPacker.extractOne(update.getCallbackQuery().getData()));
        return subscriptionService.getSubscription(subscriptionId);
    }
    private InlineKeyboardMarkup getKeyboard(int subscriptionId) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton yesButton = new InlineKeyboardButton("Да");
        yesButton.setCallbackData(CallbackQuery.SHOW_PURCHASESUCCESSMENU.getCallback() + CallbackQuery.DATA.getCallback() + subscriptionId);
        InlineKeyboardButton noButton = new InlineKeyboardButton("Нет");
        noButton.setCallbackData(CallbackQuery.SHOW_ADDSUBSMENU.getCallback());
        InlineKeyboardRow row = new InlineKeyboardRow(yesButton, noButton);
        builder.keyboardRow(row);
        return builder.build();
    }
}
