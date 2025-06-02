package dev.infiren.SubscriptionBot.bot.keyboard;

import java.util.List;

public interface CallbackDataPacker {
    String extractOne(String callbackQuery);
    List<String> extractAll(String callbackQuery);
    String pack(Object... data);
}
