package dev.infiren.SubscriptionBot.bot.message.statement;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageStatement {
    void call(Update update);
}
