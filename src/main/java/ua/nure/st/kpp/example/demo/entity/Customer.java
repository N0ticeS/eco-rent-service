package ua.nure.st.kpp.example.demo.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class Customer {
    private int id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d{9}")
    private String passportNumber;

    @NotBlank
    @Pattern(regexp = "\\+?\\d{10,15}")
    private String phoneNumber;

    private int rentalStatus;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String passportNumber, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.phoneNumber = phoneNumber;
        this.rentalStatus = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int customerId) {
        this.id = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(int rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public String getCustomerInfo() {
        return firstName + " " + lastName + " " + phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("%s: {id = %d, firstName = %s , lastName = %s, passportNumber = %s, phoneNumber = %s}", getClass().getSimpleName(), id, firstName, lastName, passportNumber, phoneNumber);
    }
}
