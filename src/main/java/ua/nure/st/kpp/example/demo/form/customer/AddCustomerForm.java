package ua.nure.st.kpp.example.demo.form.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AddCustomerForm {

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d{9}", message = "passport must be have only 9 digits")
    private String passportNumber;

    @NotBlank
    @Pattern(regexp = "\\+?\\d{10,15}", message = "Phone number must be have 10-15 digits")
    private String phoneNumber;

    private int rentalStatus;

    public AddCustomerForm() {
    }

    public AddCustomerForm(String firstName, String lastName, String passportNumber, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.phoneNumber = phoneNumber;
        this.rentalStatus = 0;
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
}
