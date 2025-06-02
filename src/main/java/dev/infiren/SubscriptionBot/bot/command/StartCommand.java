package dev.infiren.SubscriptionBot.bot.command;

import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.repository.UserRepository;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class StartCommand extends BotCommand implements Command {
    private final UserRepository userRepository;

    public StartCommand(UserRepository userRepository) {
        super("/start", "start bot");
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull String getCommand() {
        return super.getCommand();
    }

    @Override
    public void call(Update update) {
            long id = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getUserName();
            User user = new User(id, name);
            userRepository.save(user);
    }
}
