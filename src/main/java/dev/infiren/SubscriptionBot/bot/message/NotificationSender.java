package dev.infiren.SubscriptionBot.bot.message;

import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.entity.UserRecord;
import dev.infiren.SubscriptionBot.service.UserRecordService;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationSender {
    private final String paymentLink;
    private final MessageService messageService;
    private final UserService userService;
    private final UserRecordService userRecordService;

    public NotificationSender(@Value("${bot.payment.link}") String paymentLink, MessageService messageService, UserService userService, UserRecordService userRecordService) {
        this.paymentLink = paymentLink;
        this.messageService = messageService;
        this.userService = userService;
        this.userRecordService = userRecordService;
    }

    @Scheduled(cron = "@monthly")
    public void send() {
        for(User user : this.userService.getAll()) {
            List<dev.infiren.SubscriptionBot.entity.Record> recordList = this.userRecordService.getAllRecordsByUserAndActive(user.getId(), true);
            StringBuilder sb = new StringBuilder();
            sb.append("Напоминание о необходимости оплатить ваши подписки: \n\n");
            if (recordList.isEmpty()) {
                sb.append("У вас нет подписок требующих оплаты");
            } else {
                int totalSum = 0;
                for(Record record : recordList) {
                    int activeUsersAmount = 0;

                    for(UserRecord userRecord : record.getUserRecords()) {
                        if (userRecord.isActive()) {
                            ++activeUsersAmount;
                        }
                    }
                    int sum = record.getSubscriptionType().getPrice() / activeUsersAmount;
                    sb.append(String.format("Название: %s\nК оплате: %d₽\n\n", record.getSubscriptionType().getName(), sum));
                    totalSum += sum;
                }
                sb.append("Общая сумма оплаты: ");
                sb.append(totalSum);
                sb.append("₽\nСсылка для оплаты: ");
                sb.append(this.paymentLink);
            }
            this.messageService.send(user.getId(), sb.toString());
        }
    }
}
