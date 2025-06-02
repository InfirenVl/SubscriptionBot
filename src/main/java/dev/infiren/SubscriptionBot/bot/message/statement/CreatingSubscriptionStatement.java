package dev.infiren.SubscriptionBot.bot.message.statement;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.service.SubscriptionService;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class CreatingSubscriptionStatement implements MessageStatement{
    private final UserService userService;
    private final MessageService messageService;
    private final SubscriptionService subscriptionService;

    public CreatingSubscriptionStatement(UserService userService, MessageService messageService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.messageService = messageService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void call(Update update) {
        String data = update.getMessage().getText();
        User user = userService.get(update.getMessage().getChatId());
        user.addMetadata(data);
        List<String> metadata = user.getMetadata();
        userService.update(user);
        messageService.delete(update);
        if(metadata.size() == 1) {
            messageService.edit(user.getId(), user.getMenuMessageId(), "Введите цену подписки", getKeyboard());
        } else if (metadata.size() == 2) {
            messageService.edit(user.getId(), user.getMenuMessageId(), "Введите максимальное количество участников", getKeyboard());
        } else if(metadata.size() == 3) {
            try {
                Subscription subscription = new Subscription(metadata.get(0), Integer.parseInt(metadata.get(1)), Integer.parseInt(metadata.get(2)));
                subscriptionService.create(subscription);
                messageService.edit(user.getId(), user.getMenuMessageId(), "Подписка успешно создана", getKeyboard());
            } catch (NumberFormatException e) {
                messageService.edit(user.getId(), user.getMenuMessageId(), "Ошибка! Судя по всему вы ввели некорректные данные. Вернитесь и повторите попытку", getKeyboard());
            }
        } else {
            messageService.edit(user.getId(), user.getMenuMessageId(), "Вернитесь", getKeyboard());
        }
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADMINMAINMENU.name() + CallbackQuery.RESET_STATEMENTANDMETADATA.getCallback());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
