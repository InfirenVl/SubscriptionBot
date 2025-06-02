package dev.infiren.SubscriptionBot.bot.menu;

import dev.infiren.SubscriptionBot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Menu {
    void show(Update update);
}
