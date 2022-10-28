package com.narko.ServitorFOS.config;

import com.narko.ServitorFOS.service.Servitor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class ServitorInitializer {
    @Autowired
    Servitor servitor;
     @EventListener({ContextRefreshedEvent.class})
     @SneakyThrows
    public void init(){
         TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
         telegramBotsApi.registerBot(servitor);
     }
}
