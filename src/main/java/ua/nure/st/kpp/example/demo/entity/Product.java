package ua.nure.st.kpp.example.demo.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Product {

    private int id;

    @NotBlank
    private String productName;

    @NotBlank
    private String description;

    @Positive
    private double price;

    public Product() {
    }

    public Product(String productName, String description, double price) {
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int productId) {
        this.id = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return String.format("%s {productId=%d, productName='%s', description='%s', price=%.2f}",
                getClass().getSimpleName(), id, productName, description, price);
    }


}
