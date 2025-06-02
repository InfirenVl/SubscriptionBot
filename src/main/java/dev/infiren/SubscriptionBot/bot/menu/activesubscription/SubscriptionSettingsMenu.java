package dev.infiren.SubscriptionBot.bot.menu.activesubscription;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.entity.UserRecord;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackDataPacker;
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
public class SubscriptionSettingsMenu implements Menu {
    private final MessageService messageService;
    private final RecordService recordService;
    private final UserRecordService userRecordService;
    private final CallbackDataPacker callbackDataPacker;

    public SubscriptionSettingsMenu(MessageService messageService, RecordService recordService, UserRecordService userRecordService, CallbackDataPacker callbackDataPacker) {
        this.messageService = messageService;
        this.recordService = recordService;
        this.userRecordService = userRecordService;
        this.callbackDataPacker = callbackDataPacker;
    }

    @Override
    public void show(Update update) {
        Record record = recordService.getByIdWithUserRecords(Long.parseLong(callbackDataPacker.extractOne(update.getCallbackQuery().getData())));
        boolean isCreator = record.getCreator().getId() == update.getCallbackQuery().getMessage().getChatId();
        List<User> users = userRecordService.getAllUsersByRecord(record.getId());
        messageService.edit(update, getMessage(record, users), getKeyboard(record, update.getCallbackQuery().getMessage().getChatId(), isCreator, users));
    }
    private InlineKeyboardMarkup getKeyboard(Record record, long userId, boolean isCreator, List<User> users) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton changeActiveButton = new InlineKeyboardButton("Сменить статус");
        changeActiveButton.setCallbackData(CallbackQuery.SHOW_CHANGEACTIVEMENU.name() + callbackDataPacker.pack(userId, record.getId()));
        builder.keyboardRow(new InlineKeyboardRow(changeActiveButton));
        if(isCreator && record.getSubscriptionType().getMaxMembers() > 1) {
            InlineKeyboardButton regenerateCodeButton = new InlineKeyboardButton("Обновить код");
            regenerateCodeButton.setCallbackData(CallbackQuery.SHOW_REGENERATECODEMENU.name() + callbackDataPacker.pack(record.getId()));
            InlineKeyboardButton kickUserButton = new InlineKeyboardButton("Исключить");
            kickUserButton.setCallbackData(CallbackQuery.SHOW_KICKUSERMENU.name() + callbackDataPacker.pack(record.getId()));
            InlineKeyboardRow row = new InlineKeyboardRow(regenerateCodeButton, kickUserButton);
            builder.keyboardRow(row);
        }
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ACTIVESUBSMENU.name());
        InlineKeyboardButton deleteButton = new InlineKeyboardButton("Удалить");
        if (!isCreator || users.size() == 1)
            deleteButton.setCallbackData(CallbackQuery.SHOW_SUBDELETEMENU.name() + callbackDataPacker.pack(record.getId()));
        else
            deleteButton.setCallbackData(CallbackQuery.SHOW_CHANGESUBCREATORMENU.name() + callbackDataPacker.pack(record.getId()));
        InlineKeyboardRow row = new InlineKeyboardRow(deleteButton, backButton);
        builder.keyboardRow(row);
        return builder.build();
    }
    private String getMessage(Record record, List<User> users) {
        StringBuilder sb = new StringBuilder();
        int amountOfActiveUsers = record.getUserRecords().stream().filter(UserRecord::isActive).toList().size();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        sb.append(String.format("Название: %s\nДата следующего платежа: %s\nМаксимальное количество участников: %d\n",
                record.getSubscriptionType().getName(),
                record.getDateOfPayment().format(formatter),
                record.getSubscriptionType().getMaxMembers()));
        if(amountOfActiveUsers == 0)
            sb.append(String.format("Стоимость платежа: %d₽\n", 0));
        else
            sb.append(String.format("Стоимость платежа: %d₽\n", record.getSubscriptionType().getPrice() / amountOfActiveUsers));
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
        }
        return sb.toString();
    }
}
