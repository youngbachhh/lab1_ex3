package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private User user;
    private double total;
    private List<Book> books;
    private String status;

    public Order() {
        total = 0;
        books = new ArrayList<>();
        status = "unconfirmed";
    }

    public Order(int id, User user, double total, List<Book> books, String status) {
        this.id = id;
        this.user = user;
        this.total = total;
        this.books = books;
        this.status = status;
    }

    public Order(int id, User user, double total, String status) {
        this.id = id;
        this.user = user;
        this.total = total;
        this.books = new ArrayList<>();
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

}
