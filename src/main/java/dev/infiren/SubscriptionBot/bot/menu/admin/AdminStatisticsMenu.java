package dev.infiren.SubscriptionBot.bot.menu.admin;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.service.RecordService;
import dev.infiren.SubscriptionBot.service.SubscriptionService;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminStatisticsMenu implements Menu {
    private final MessageService messageService;
    private final UserService userService;
    private final RecordService recordService;
    private final SubscriptionService subscriptionService;


    public AdminStatisticsMenu(MessageService messageService, UserService userService, RecordService recordService, SubscriptionService subscriptionService) {
        this.messageService = messageService;
        this.userService = userService;
        this.recordService = recordService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void show(Update update) {
        List<User> allUsers = userService.getAll();
        List<Record> allRecords = recordService.getAll();
        List<Subscription> allSubs = subscriptionService.getAllSubscriptions();
        messageService.edit(update, getMessage(allUsers, allRecords, allSubs), getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_ADMINMAINMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();
    }

    private String getMessage(List<User> allUsers, List<Record> allRecords, List<Subscription> allSubs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Общее число пользователей: ");
        sb.append(allUsers.size());
        sb.append("\n\n");
        sb.append("Общее число подписок: ");
        sb.append(allRecords.size());
        sb.append("\nИз них: ");
        int familyCounter = 0;
        int soloCounter = 0;
        for(Record record : allRecords) {
            if(record.getSubscriptionType().getMaxMembers() > 1)
                familyCounter++;
            else
                soloCounter++;
        }
        sb.append("\n   Одиночные: ");
        sb.append(soloCounter);
        sb.append("\n   Семейные: ");
        sb.append(familyCounter);
        sb.append("\nПо типу подписки: ");
        for (Subscription subscription : allSubs) {
            List<Record> removeRecordList = new ArrayList<>();
            sb.append("\n   ");
            sb.append(subscription.getName());
            sb.append(": ");
            int counter = 0;
            for (Record record : allRecords){
                if (record.getSubscriptionType().equals(subscription)){
                    removeRecordList.add(record);
                    counter++;
                }
            }
            allRecords.removeAll(removeRecordList);
            sb.append(counter);
        }
        return sb.toString();
    }
}
