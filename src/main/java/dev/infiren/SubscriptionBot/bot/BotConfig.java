package dev.infiren.SubscriptionBot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class BotConfig {
    @Value("${bot.token}")
    private String token;
    private LongPollingUpdateConsumer messageHandler;

    @Lazy
    @Autowired
    public void setMessageHandler(LongPollingUpdateConsumer messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(token, messageHandler);
    }

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }
}
