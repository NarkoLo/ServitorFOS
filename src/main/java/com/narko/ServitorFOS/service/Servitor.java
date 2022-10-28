package com.narko.ServitorFOS.service;

import com.narko.ServitorFOS.config.ServitorConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Servitor extends TelegramLongPollingBot {
    final ServitorConfig config;
    private final List<Update> updates = new ArrayList<>();
    private final AtomicBoolean enable = new AtomicBoolean();
    @Autowired
    Parser parser;

    public Servitor(ServitorConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getServitorName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            if(updates.stream().noneMatch(up -> Objects.equals(update.getMessage().getChatId(), update.getMessage().getChatId()))){
                updates.add(update);
                enable.set(true);
                System.out.println("Chat id received");
            }
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                case "/orders" -> {

                    sendOrders(chatId);
                }
                default -> sendMessage(chatId, "Error command not found");
            }
        }
    }
    private void startCommandReceived(Long chatId, String firstName){
        String answer = "Приветствую вас повелитель " + firstName;
        sendMessage(chatId,answer);
    }
    @SneakyThrows({TelegramApiValidationException.class, TelegramApiException.class})
    private void sendMessage(Long chatId,String answer)  {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(answer);
        execute(message);

    }
    private void sendOrders(Long chatId){
        for (int i = 0; i < parser.ordersAmount(); i++) {
            sendMessage(chatId,parser.getOrder());
        }
    }
    @Scheduled(fixedRate =60000 )
    private void updateOrders(){
        if(enable.get()){

            parser.updateListOrders();
            for (Update up: updates) {
                for (int i = 0; i < parser.ordersAmount(); i++) {
                    if(!parser.getOrder().isEmpty() ){
                        sendMessage(up.getMessage().getChatId(),parser.getOrder());
                        System.out.println("Update successful");
                    }
                }
            }
        }
    }
}