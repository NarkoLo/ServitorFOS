package com.narko.ServitorFOS.data;

import lombok.Data;

import java.util.Objects;
@Data
public class Order {
    private String title;
    private boolean isSent;

    private Long id;

    private String link = "https://freelance.habr.com";

    public void setLink(String link) {
        this.link += link;
    }

    private String price;
    private String tags;
    public Order(String title, String link, String price, String tags, Long id) {
        this.title = title;
        this.link = this.link + link;
        this.price = price;
        this.tags = tags;
        this.id = id;
    }
    @Override
    public String toString() {
        return "\nНазвание заказа: "+ title + "\nЦена: " + price + "\nСсылка на заказ: "
                + link +"\nТэги: " + tags;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(null == obj || this.getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        if(!Objects.equals(title, order.title)) return false;
        return Objects.equals(link, order.link);
    }

    @Override
    public int hashCode() {
        int result = title == null ? 0: title.hashCode();
        result += 31*id;
        return result;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
