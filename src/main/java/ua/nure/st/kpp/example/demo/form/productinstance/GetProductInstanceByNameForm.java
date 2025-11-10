package ua.nure.st.kpp.example.demo.form.productinstance;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetProductInstanceByNameForm {
    @NotNull
    @Size(min = 2, max = 20)
    private String productName;

    public GetProductInstanceByNameForm() {}

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}
}
