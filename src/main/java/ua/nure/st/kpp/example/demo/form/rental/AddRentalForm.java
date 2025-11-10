package ua.nure.st.kpp.example.demo.form.rental;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;


public class AddRentalForm {

    @NotNull
    private Integer productInstanceId;

    @NotNull
    private Integer customerId;

    @NotNull
    private Date rentalDate;

    @NotNull
    private Date returnDate;

    public Integer getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(Integer productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
}
