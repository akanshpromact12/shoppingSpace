package com.promact.akansh.shoppingappdemo.Model;

/**
 * Created by Akansh on 04-10-2017.
 */

public class Product {
    private String productName;
    private String productDesc;
    private double sellingPrice;

    public Product() {

    }

    public Product(Product product) {
        this.productName = product.getProductName();
        this.productDesc = product.getProductDesc();
        this.sellingPrice = product.getSellingPrice();
    }

    public Product(String productName, String productDesc, Double sellingPrice) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.sellingPrice = sellingPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
