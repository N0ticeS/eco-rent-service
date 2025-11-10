package ua.nure.st.kpp.example.demo.form.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GetCustomerByLastNameForm {
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    public GetCustomerByLastNameForm() {}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

}
