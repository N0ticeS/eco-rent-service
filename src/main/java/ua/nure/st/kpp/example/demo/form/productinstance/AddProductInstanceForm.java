package ua.nure.st.kpp.example.demo.form.productinstance;

import ua.nure.st.kpp.example.demo.entity.Product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AddProductInstanceForm {
    @NotNull
    private Product product;

    public AddProductInstanceForm(Product product) {
        this.product = product;
    }

    public AddProductInstanceForm() {}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}
}
