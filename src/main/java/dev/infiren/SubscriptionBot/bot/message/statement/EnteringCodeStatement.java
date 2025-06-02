package dev.infiren.SubscriptionBot.bot.message.statement;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.TooManyMembersException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class EnteringCodeStatement implements MessageStatement {
    private final RecordService recordService;
    private final UserService userService;
    private final MessageService messageService;
    private final UserRecordService userRecordService;

    public EnteringCodeStatement(RecordService recordService, UserService userService, MessageService messageService, UserRecordService userRecordService) {
        this.recordService = recordService;
        this.userService = userService;
        this.messageService = messageService;
        this.userRecordService = userRecordService;
    }

    @Override
    public void call(Update update) {
        String code = update.getMessage().getText();
        User user;
        user = userService.get(update.getMessage().getChatId());
        messageService.delete(update);
        try {
            userRecordService.createByCode(code, update.getMessage().getChatId());
        } catch (RecordNotFoundException e) {
            messageService.edit(user.getId(), user.getMenuMessageId(), "Код недействителен. Попробуйте другой код и повторите попытку", getKeyboard(false));
            return;
        } catch (TooManyMembersException e) {
            messageService.edit(user.getId(), user.getMenuMessageId(), "У этой подписки уже максимум участников. Попробуйте другой код и повторите попытку", getKeyboard(false));
            return;
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        messageService.edit(user.getId(), user.getMenuMessageId(), "Подписка успешно добавлена. Вы можете вернуться или ввести ещё один код", getKeyboard(true));
    }
    private InlineKeyboardMarkup getKeyboard(boolean isSuccess) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton;
        if(isSuccess)
            backButton = new InlineKeyboardButton("Вернуться");
        else
            backButton = new InlineKeyboardButton("Отменить");
        backButton.setCallbackData(CallbackQuery.SHOW_ADDSUBSMENU.getCallback() + CallbackQuery.RESET_STATEMENTANDMETADATA.getCallback());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
}
