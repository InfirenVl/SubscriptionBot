package dev.infiren.SubscriptionBot.bot.command;

import dev.infiren.SubscriptionBot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    void call(Update update);
    String getCommand();
}
