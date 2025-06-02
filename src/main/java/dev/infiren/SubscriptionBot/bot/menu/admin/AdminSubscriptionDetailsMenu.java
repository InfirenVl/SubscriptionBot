package dev.infiren.SubscriptionBot.bot.menu.admin;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.entity.UserRecord;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AdminSubscriptionDetailsMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;

    public AdminSubscriptionDetailsMenu(MessageService messageService, RecordService recordService, UserRecordService userRecordService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        Record record = recordService.getByIdWithUserRecords(Long.parseLong(callbackDataPacker.extractOne(update.getCallbackQuery().getData())));
        messageService.edit(update, getMessage(record), getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADMINSUBSMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }
    private String getMessage(Record record) {
        StringBuilder sb = new StringBuilder();
        List<User> users = userRecordService.getAllUsersByRecord(record.getId());
        int amountOfActiveUsers = record.getUserRecords().stream().filter(UserRecord::isActive).toList().size();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        sb.append(String.format("Название: %s\nДата следующего платежа: %s\nМаксимальное количество участников: %d\n",
                record.getSubscriptionType().getName(),
                record.getDateOfPayment().format(formatter),
                record.getSubscriptionType().getMaxMembers()));
        if(amountOfActiveUsers == 0)
            sb.append(String.format("Сумма платежа: %d₽\n", 0));
        else
            sb.append(String.format("Сумма платежа: %d₽\n", record.getSubscriptionType().getPrice() / amountOfActiveUsers));
        if(record.getCode() != null)
            sb.append(String.format("Код: %s\n", record.getCode()));
        if(record.getSubscriptionType().getMaxMembers() > 1) {
            sb.append(String.format("Общая стоимость подписки: %d₽\n", record.getSubscriptionType().getPrice()));
            sb.append("Пользователи:\n");
            int i = 1;
            for (User user : users) {
                sb.append(i);
                sb.append(". ");
                sb.append(user.getName());
                for(UserRecord userRecord : record.getUserRecords()){
                    if(userRecord.getUser().getId() == user.getId() && !userRecord.isActive())
                        sb.append(" (не активен)");
                }
                sb.append("\n");
                i++;
            }
        } else {
            sb.append("Создатель: ");
            sb.append(record.getCreator().getName());
        }
        return sb.toString();
    }
}
