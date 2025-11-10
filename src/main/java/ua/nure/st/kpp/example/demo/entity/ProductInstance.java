package ua.nure.st.kpp.example.demo.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductInstance {
    private int id;

    @NotNull
    private Product product;

    @Positive
    private double rentalPrice;

    @Positive
    private int rentalRate;

    public ProductInstance() {
        this.rentalRate = 100;
    }

    public ProductInstance(Product product) {
        this.product = product;
        this.rentalRate = 100;
    }

    public int getId() {
        return id;
    }

    public void setId(int instanceId) {
        this.id = instanceId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public int getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(int rentalRate) {
        this.rentalRate = rentalRate;
    }

    public String getDisplayProductInfo() {
        return this.id + "," + this.product.getProductName();
    }

    @Override
    public String toString() {
        return String.format("%s {instanceId=%d, productId=%d, rentalPrice=%.2f, rentalRate=%d}",
                getClass().getSimpleName(), id, product.getId(), rentalPrice, rentalRate);
    }


}