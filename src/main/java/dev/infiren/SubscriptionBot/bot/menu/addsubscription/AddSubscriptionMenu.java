package dev.infiren.SubscriptionBot.bot.menu.addsubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.service.SubscriptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class AddSubscriptionMenu implements Menu {
    private final MessageService messageService;
    private final SubscriptionService subscriptionService;
    private final String MENU_TEXT = "Добавить подписку";

    public AddSubscriptionMenu(MessageService messageService, SubscriptionService subscriptionService) {
        this.messageService = messageService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void show(Update update) {
        messageService.edit(update, MENU_TEXT, getKeyboard(subscriptionService.getAllSubscriptions()));
    }

    private InlineKeyboardMarkup getKeyboard(List<Subscription> subscriptionList) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        for (int i = 0; i <= subscriptionList.size() - 2; i += 2) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(subscriptionList.get(i)), getButton(subscriptionList.get(i+1)));
            builder.keyboardRow(row);
        }
        if(subscriptionList.size() % 2 != 0) {
            InlineKeyboardRow row = new InlineKeyboardRow(getButton(subscriptionList.getLast()));
            builder.keyboardRow(row);
        }
        InlineKeyboardButton makeRequestButton = new InlineKeyboardButton("Оставить заявку");
        makeRequestButton.setCallbackData(CallbackQuery.SHOW_REQUESTENTERMENU.getCallback());
        InlineKeyboardButton useCodeButton = new InlineKeyboardButton("Активировать код");
        useCodeButton.setCallbackData(CallbackQuery.SHOW_USECODEMENU.getCallback());
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("В главное меню");
        mainMenuButton.setCallbackData(CallbackQuery.SHOW_MAINMENU.getCallback());
        builder.keyboardRow(new InlineKeyboardRow(makeRequestButton));
        builder.keyboardRow(new InlineKeyboardRow(useCodeButton));
        builder.keyboardRow(new InlineKeyboardRow(mainMenuButton));
        return builder.build();
    }

    private InlineKeyboardButton getButton(Subscription subscription) {
        InlineKeyboardButton button = new InlineKeyboardButton(subscription.getName() + " (" + subscription.getPrice() + "₽)");
        button.setCallbackData(CallbackQuery.SHOW_PURCHASECONFIRMMENU.getCallback() + CallbackQuery.DATA.getCallback() + subscription.getId());
        return button;
    }

}
