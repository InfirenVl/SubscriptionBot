package dev.infiren.SubscriptionBot.bot.command;

import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.MessageService;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.service.UserService;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
@Component
public class MenuCommand extends BotCommand implements Command{
    private final MessageService messageService;
    private final UserService userService;
    private final Menu mainMenu;

    public MenuCommand(MessageService messageService, UserService userService, Menu mainMenu) {
        super("/menu", "recreate menu");
        this.messageService = messageService;
        this.userService = userService;
        this.mainMenu = mainMenu;
    }

    @Override
    public void call(Update update) {
        User user = userService.get(update.getMessage().getChatId());
        int menuMessageId = user.getMenuMessageId();
        messageService.delete(update);
        if(menuMessageId > 0)
            mainMenu.show(update);
        user.setMenuMessageId(-2);
        userService.update(user);
        messageService.delete(user.getId(), menuMessageId);
    }
}
