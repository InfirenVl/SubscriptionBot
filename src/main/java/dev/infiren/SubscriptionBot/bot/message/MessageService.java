package dev.infiren.SubscriptionBot.bot.message;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MessageService {
    void send(long chatId, String messageText);
    void send(long chatId, String messageText, InlineKeyboardMarkup keyboard);
    void edit(Update update, String messageText);
    void edit(Update update, String messageText, InlineKeyboardMarkup keyboard);
    void edit(long chatId, int messageId, String messageText);
    void edit(long chatId, int messageId, String messageText, InlineKeyboardMarkup keyboard);
    void delete(Update update);
    void delete(long chatId, int messageId);
}
