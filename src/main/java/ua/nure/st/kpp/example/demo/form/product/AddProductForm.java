package ua.nure.st.kpp.example.demo.form.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class AddProductForm {

    @NotBlank
    @Size(min = 2, max = 100)
    private String productName;

    @NotBlank
    @Size(min = 5, max = 500)
    private String description;

    @Positive
    private double price;

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}
}
