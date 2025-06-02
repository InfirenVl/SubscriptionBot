package dev.infiren.SubscriptionBot.bot.message.statement;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.service.RequestService;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class EnteringRequestStatement implements MessageStatement {
    private final UserService userService;
    private final RequestService requestService;
    private final MessageService messageService;

    public EnteringRequestStatement(UserService userService, RequestService requestService, MessageService messageService) {
        this.userService = userService;
        this.requestService = requestService;
        this.messageService = messageService;
    }

    @Override
    public void call(Update update) {
        String requestText = update.getMessage().getText();
        User user = userService.get(update.getMessage().getChatId());
        messageService.delete(update);
        requestService.create(requestText, user);
        messageService.edit(user.getId(), user.getMenuMessageId(), "Ваш запрос успешно отправлен. Вы можете написать ещё один или вернуться назад", getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADDSUBSMENU.getCallback() + CallbackQuery.RESET_STATEMENTANDMETADATA.getCallback());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
