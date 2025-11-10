package ua.nure.st.kpp.example.demo.entity;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Date;

public class Rental {
    private int id;

    @NotNull
    private ProductInstance productInstance;

    @NotNull
    private Customer customer;

    @NotNull
    @FutureOrPresent
    private Date rentalDate;

    @NotNull
    @FutureOrPresent
    private Date returnDate;

    @Positive
    private double rentalPrice;

    public Rental() {
    }

    public Rental(ProductInstance productInstance, Customer customer, Date rentalDate, Date returnDate) {
        this.productInstance = productInstance;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductInstance getProductInstance() {
        return productInstance;
    }

    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    @Override
    public String toString() {
        return String.format("%s {rentalId=%d, productInstanceId=%d, customerId=%d, rentalDate=%s, returnDate=%s, rentalPrice=%.2f}",
                getClass().getSimpleName(), id, productInstance.getId(), customer.getId(), rentalDate, returnDate, rentalPrice);
    }


}
