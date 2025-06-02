package dev.infiren.SubscriptionBot.bot.message;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class MessageServiceImpl implements MessageService {
    private final TelegramClient telegramClient;

    public MessageServiceImpl(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    @SneakyThrows
    public void send(long chatId, String messageText) {
        SendMessage message = new SendMessage(String.valueOf(chatId), messageText);
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void send(long chatId, String messageText, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage(String.valueOf(chatId), messageText);
        message.setReplyMarkup(keyboard);
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void edit(Update update, String messageText) {
        EditMessageText message = new EditMessageText(messageText);
        if (update.hasCallbackQuery()) {
            message.setChatId(update.getCallbackQuery().getMessage().getChatId());
            message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        }
        else {
            message.setChatId(update.getMessage().getChatId());
            message.setMessageId(update.getMessage().getMessageId());
        }
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void edit(Update update, String messageText, InlineKeyboardMarkup keyboard) {
        EditMessageText message = new EditMessageText(messageText);
        if (update.hasCallbackQuery()) {
            message.setChatId(update.getCallbackQuery().getMessage().getChatId());
            message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        }
        else {
            message.setChatId(update.getMessage().getChatId());
            message.setMessageId(update.getMessage().getMessageId());
        }
        message.setReplyMarkup(keyboard);
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void edit(long chatId, int messageId, String messageText) {
        EditMessageText message = new EditMessageText(messageText);
        message.setChatId(chatId);
        message.setMessageId(messageId);
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void edit(long chatId, int messageId, String messageText, InlineKeyboardMarkup keyboard) {
        EditMessageText message = new EditMessageText(messageText);
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setReplyMarkup(keyboard);
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void delete(Update update) {
        DeleteMessage message = new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId());
        telegramClient.execute(message);
    }

    @Override
    @SneakyThrows
    public void delete(long chatId, int messageId) {
        DeleteMessage message = new DeleteMessage(String.valueOf(chatId), messageId);
        telegramClient.execute(message);
    }




}
