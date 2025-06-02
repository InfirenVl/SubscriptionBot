package dev.infiren.SubscriptionBot.bot;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

public class TelegramBot implements SpringLongPollingBot {
    private final String token;
    private final LongPollingUpdateConsumer messageHandler;

    public TelegramBot(String token, LongPollingUpdateConsumer messageHandler) {
        this.token = token;
        this.messageHandler = messageHandler;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return messageHandler;
    }
}
