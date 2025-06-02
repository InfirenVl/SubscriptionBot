package dev.infiren.SubscriptionBot.bot.message;

import dev.infiren.SubscriptionBot.bot.command.Command;
import dev.infiren.SubscriptionBot.bot.keyboard.CallbackQuery;
import dev.infiren.SubscriptionBot.bot.menu.Menu;
import dev.infiren.SubscriptionBot.bot.message.statement.MessageStatement;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Component
public class MessageHandler implements LongPollingSingleThreadUpdateConsumer {
    private final UserService userService;
    private final Command menuCommand;
    private final Command startCommand;
    private final Menu mainMenu;
    private final Menu addSubscriptionMenu;
    private final Menu purchaseConfirmMenu;
    private final Menu purchaseSuccessMenu;
    private final Menu activeSubscriptionMenu;
    private final Menu subscriptionSettingsMenu;
    private final Menu subscriptionDeleteMenu;
    private final Menu useCodeMenu;
    private final Menu requestEnterMenu;
    private final Menu regenerateCodeMenu;
    private final Menu kickUserMenu;
    private final Menu kickUserSuccessMenu;
    private final Menu adminMainMenu;
    private final Menu adminRequestMenu;
    private final Menu adminRequestListMenu;
    private final Menu adminRequestDeleteMenu;
    private final Menu adminAddSubscriptionMenu;
    private final Menu changeActiveMenu;
    private final Menu adminStatisticsMenu;
    private final Menu adminSubscriptionDetailsMenu;
    private final Menu adminSubscriptionsMenu;
    private final Menu changeSubscriptionCreatorMenu;
    private final Menu changeSubscriptionCreatorSuccessMenu;
    private final MessageStatement enteringCodeStatement;
    private final MessageStatement enteringRequestStatement;
    private final MessageStatement creatingSubscriptionStatement;
    private final MessageService messageService;

    public MessageHandler(Command startCommand, UserService userService, Command menuCommand, Menu mainMenu, Menu addSubscriptionMenu, Menu purchaseConfirmMenu, Menu purchaseSuccessMenu, Menu activeSubscriptionMenu, Menu subscriptionSettingsMenu, Menu subscriptionDeleteMenu, Menu useCodeMenu, Menu requestEnterMenu, Menu regenerateCodeMenu, Menu kickUserMenu, Menu kickUserSuccessMenu, Menu adminMainMenu, Menu adminRequestMenu, Menu adminRequestListMenu, Menu adminRequestDeleteMenu, Menu adminAddSubscriptionMenu, Menu changeActiveMenu, Menu adminStatisticsMenu, Menu adminSubscriptionDetailsMenu, Menu adminSubscriptionsMenu, Menu changeSubscriptionCreatorMenu, Menu changeSubscriptionCreatorSuccessMenu, MessageStatement enteringCodeStatement, MessageStatement enteringRequestStatement, MessageStatement creatingSubscriptionStatement, MessageService messageService) {
        this.startCommand = startCommand;
        this.userService = userService;
        this.menuCommand = menuCommand;
        this.mainMenu = mainMenu;
        this.addSubscriptionMenu = addSubscriptionMenu;
        this.purchaseConfirmMenu = purchaseConfirmMenu;
        this.purchaseSuccessMenu = purchaseSuccessMenu;
        this.activeSubscriptionMenu = activeSubscriptionMenu;
        this.subscriptionSettingsMenu = subscriptionSettingsMenu;
        this.subscriptionDeleteMenu = subscriptionDeleteMenu;
        this.useCodeMenu = useCodeMenu;
        this.requestEnterMenu = requestEnterMenu;
        this.regenerateCodeMenu = regenerateCodeMenu;
        this.kickUserMenu = kickUserMenu;
        this.kickUserSuccessMenu = kickUserSuccessMenu;
        this.adminMainMenu = adminMainMenu;
        this.adminRequestMenu = adminRequestMenu;
        this.adminRequestListMenu = adminRequestListMenu;
        this.adminRequestDeleteMenu = adminRequestDeleteMenu;
        this.adminAddSubscriptionMenu = adminAddSubscriptionMenu;
        this.changeActiveMenu = changeActiveMenu;
        this.adminStatisticsMenu = adminStatisticsMenu;
        this.adminSubscriptionDetailsMenu = adminSubscriptionDetailsMenu;
        this.adminSubscriptionsMenu = adminSubscriptionsMenu;
        this.changeSubscriptionCreatorMenu = changeSubscriptionCreatorMenu;
        this.changeSubscriptionCreatorSuccessMenu = changeSubscriptionCreatorSuccessMenu;
        this.enteringCodeStatement = enteringCodeStatement;
        this.enteringRequestStatement = enteringRequestStatement;
        this.creatingSubscriptionStatement = creatingSubscriptionStatement;
        this.messageService = messageService;
    }

    @Override
    public void consume(Update update) {
        String message = getMessage(update);
        long id = getId(update);
        User user = getUser(id, update);
        if(sendNotAuthMessage(id, user, message))
            return;

        setMenuMessageId(update, user);


        if (message != null && message.startsWith("/")) {
            if (message.equals(startCommand.getCommand()) && user == null) {
                startCommand.call(update);
                mainMenu.show(update);
            } else if (message.equals(menuCommand.getCommand()) && user != null) {
                menuCommand.call(update);
            }
        } else if (update.hasCallbackQuery()) {
            String rawCallbackQuery = update.getCallbackQuery().getData();
            CallbackQuery callbackQuery;
            if (rawCallbackQuery.contains("|"))
                callbackQuery = CallbackQuery.valueOf(rawCallbackQuery.substring(0, rawCallbackQuery.indexOf("|")));
            else
                callbackQuery = CallbackQuery.valueOf(rawCallbackQuery);
            switch (callbackQuery) {
                case SHOW_MAINMENU -> mainMenu.show(update);
                case SHOW_ACTIVESUBSMENU -> activeSubscriptionMenu.show(update);
                case SHOW_ADDSUBSMENU -> addSubscriptionMenu.show(update);
                case SHOW_PURCHASECONFIRMMENU -> purchaseConfirmMenu.show(update);
                case SHOW_PURCHASESUCCESSMENU -> purchaseSuccessMenu.show(update);
                case SHOW_SUBSETTINGSMENU -> subscriptionSettingsMenu.show(update);
                case SHOW_SUBDELETEMENU -> subscriptionDeleteMenu.show(update);
                case SHOW_USECODEMENU -> useCodeMenu.show(update);
                case SHOW_REQUESTENTERMENU -> requestEnterMenu.show(update);
                case SHOW_REGENERATECODEMENU -> regenerateCodeMenu.show(update);
                case SHOW_KICKUSERMENU -> kickUserMenu.show(update);
                case SHOW_KICKUSERSUCCESSMENU -> kickUserSuccessMenu.show(update);
                case SHOW_ADMINMAINMENU -> adminMainMenu.show(update);
                case SHOW_ADMINREQUESTLISTMENU -> adminRequestListMenu.show(update);
                case SHOW_ADMINREQUESTMENU -> adminRequestMenu.show(update);
                case SHOW_ADMINREQUESTDELETEMENU -> adminRequestDeleteMenu.show(update);
                case SHOW_ADMINADDSUBMENU -> adminAddSubscriptionMenu.show(update);
                case SHOW_CHANGEACTIVEMENU -> changeActiveMenu.show(update);
                case SHOW_ADMINSTATISTICSMENU -> adminStatisticsMenu.show(update);
                case SHOW_ADMINSUBSMENU -> adminSubscriptionsMenu.show(update);
                case SHOW_ADMINSUBDETAILSMENU -> adminSubscriptionDetailsMenu.show(update);
                case SHOW_CHANGESUBCREATORMENU -> changeSubscriptionCreatorMenu.show(update);
                case SHOW_CHANGESUBCREATORSUCCESSMENU -> changeSubscriptionCreatorSuccessMenu.show(update);
            }
        } else if (user != null && user.getStatement() != User.Statement.NONE && message != null) {
            switch (user.getStatement()) {
                case ENTERING_CODE -> enteringCodeStatement.call(update);
                case ENTERING_REQUEST -> enteringRequestStatement.call(update);
                case CREATING_SUBSCRIPTION -> creatingSubscriptionStatement.call(update);
            }
        }
    }
    private String getMessage(Update update) {
        if(update.hasMessage() && update.getMessage().hasText())
            return update.getMessage().getText();
        return null;
    }
    private Long getId(Update update) {
        if(update.hasMessage())
            return update.getMessage().getChatId();
        else if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId();
        else
            return -1L;
    }
    private User getUser(long id, Update update) {
        try {
            User user = userService.get(id);
            if (update.hasCallbackQuery() && update.getCallbackQuery().getData().contains(CallbackQuery.RESET_STATEMENTANDMETADATA.getCallback())) {
                user.setStatement(User.Statement.NONE);
                user.resetMetadata();
                userService.update(user);
            }
            return user;
        } catch (UserNotFoundException e) {
            return null;
        }
    }
    private void setMenuMessageId(Update update, User user) {
        if (user != null) {
            if(update.hasCallbackQuery()) {
                if(user.getMenuMessageId() < 0) {
                    user.setMenuMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    userService.update(user);
                }
            }
        }
    }
    private boolean sendNotAuthMessage(long id, User user, String message) {
        if (user == null && !Objects.equals(message, startCommand.getCommand())) {
            messageService.send(id, "Чтобы начать работать с ботом сначала нужно отправить команду /start");
            return true;
        }
        return false;
    }
}
