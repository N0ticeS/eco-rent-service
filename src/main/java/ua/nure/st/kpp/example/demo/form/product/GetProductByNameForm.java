package ua.nure.st.kpp.example.demo.form.product;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GetProductByNameForm {

    @NotBlank
    @Size(min = 2, max = 500)
    private String productName;

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

}
