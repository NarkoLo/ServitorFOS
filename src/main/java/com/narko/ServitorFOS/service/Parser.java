package com.narko.ServitorFOS.service;

import com.narko.ServitorFOS.config.ParserConfig;
import com.narko.ServitorFOS.config.ServitorConfig;
import com.narko.ServitorFOS.data.Order;
import lombok.SneakyThrows;
import lombok.Value;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Parser {
    final ParserConfig config;
    private final AtomicLong counter = new AtomicLong();
    Document doc;
    final List<Order> orderList = new ArrayList<>();
    Elements postsElements;
    @SneakyThrows
    public Parser(ParserConfig config) {
        this.config = config;
        doc = Jsoup.connect(config.getUrl()).get();
        init();
    }

    @SneakyThrows
    public void init(){
        System.out.println("Parser init successful");
        postsElements = doc.getElementsByClass("content-list__item");
        postsElements.forEach(postsElement -> orderList.add(new Order(postsElement.getElementsByClass("task__title").attr("title"),
                postsElement.getElementsByTag("a").attr("href"),
                postsElement.select("span.count").text() +postsElement.select("span.negotiated_price").text(),
                postsElement.select("a.tags__item_link").text(), counter.incrementAndGet())));
    }

    public void updateListOrders(){
        for (Element postsElement:postsElements) {
            Order newOrder = new Order(postsElement.getElementsByClass("task__title").attr("title"),
                    postsElement.getElementsByTag("a").attr("href"),
                    postsElement.select("span.count").text() +postsElement.select("span.negotiated_price").text() ,
                    postsElement.getElementsByClass("tags__item_link").val(), counter.incrementAndGet());
            newOrder.setSent(false);
            if(originalityCheck(newOrder)) orderList.add(newOrder);
        }
    }
    public String getOrder(){
        String message = "";
        for (Order order : orderList) {
            if (!order.isSent()) {
                message =order.toString();
                order.setSent(true);
                return message;
            }
        }
        return message;
    }
    public int ordersAmount(){
        return orderList.size();
    }
    private boolean originalityCheck(Order newOrder){
        return orderList.stream().noneMatch(order -> order.equals(newOrder));
    }
}
