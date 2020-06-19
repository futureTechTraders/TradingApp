package com.example.tradingapp;

public class StockClosingPrice {

    private double previousPrice;

    public StockClosingPrice() {

        previousPrice = 0;
    }

    public double getPrice() {

        return previousPrice;
    }

    public void setPrice(double price) {

        previousPrice = price;
    }
}
