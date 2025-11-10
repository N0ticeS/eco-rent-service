package ua.nure.st.kpp.example.demo.form.rental;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GetRentalByIdForm {

    @NotNull()
    @Min(value = 1)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
