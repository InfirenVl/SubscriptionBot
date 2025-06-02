package dev.infiren.SubscriptionBot.bot.menu.admin;

import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AdminMainMenu implements Menu {
    private final MessageService messageService;
    private final String MENU_TEXT = "Меню администратора";

    public AdminMainMenu(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void show(Update update) {
        messageService.edit(update, MENU_TEXT, getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> builder = InlineKeyboardMarkup.builder();
        InlineKeyboardButton statisticsButton = new InlineKeyboardButton("Статистика");
        statisticsButton.setCallbackData(CallbackQuery.SHOW_ADMINSTATISTICSMENU.name());
        InlineKeyboardButton requestsButton = new InlineKeyboardButton("Заявки");
        requestsButton.setCallbackData(CallbackQuery.SHOW_ADMINREQUESTLISTMENU.name());
        InlineKeyboardButton subsButton = new InlineKeyboardButton("Подписки");
        subsButton.setCallbackData(CallbackQuery.SHOW_ADMINSUBSMENU.name());
        InlineKeyboardButton addSubButton = new InlineKeyboardButton("  Новая подписка  ");
        addSubButton.setCallbackData(CallbackQuery.SHOW_ADMINADDSUBMENU.name());
        InlineKeyboardButton backButton = new InlineKeyboardButton("Вернуться");
        backButton.setCallbackData(CallbackQuery.SHOW_MAINMENU.name());
        builder.keyboardRow(new InlineKeyboardRow(statisticsButton, subsButton));
        builder.keyboardRow(new InlineKeyboardRow(addSubButton, requestsButton));
        builder.keyboardRow(new InlineKeyboardRow(backButton));
        return builder.build();

    }
}
