package dev.infiren.SubscriptionBot.bot.keyboard;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallbackDataPackerImpl implements CallbackDataPacker {
    private final String SEPARATOR = "@";
    @Override
    public String extractOne(String callbackQuery) {
        String data;
        data = callbackQuery.substring(callbackQuery.indexOf("|"));
        data = data.replace(CallbackQuery.DATA.getCallback(), "");
        data = data.replace(SEPARATOR, "");
        return data;
    }
    public List<String> extractAll(String callbackQuery) {
        String data;
        data = callbackQuery.substring(callbackQuery.indexOf("|"));
        data = data.replace(CallbackQuery.DATA.getCallback(), "");
        return List.of(data.split(SEPARATOR));
    }
    public String pack(Object... data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CallbackQuery.DATA.getCallback());
        for(Object object : data) {
            stringBuilder.append(object);
            stringBuilder.append(SEPARATOR);
        }
        return stringBuilder.substring(0, stringBuilder.length()-1);
    }
}
