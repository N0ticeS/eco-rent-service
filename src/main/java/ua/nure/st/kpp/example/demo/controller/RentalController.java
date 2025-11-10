package ua.nure.st.kpp.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.st.kpp.example.demo.entity.Rental;
import ua.nure.st.kpp.example.demo.form.rental.AddRentalForm;
import ua.nure.st.kpp.example.demo.form.rental.GetRentalByIdForm;
import ua.nure.st.kpp.example.demo.service.*;

import javax.validation.Valid;
import java.util.List;


@Controller
public class RentalController {

    Logger log = LoggerFactory.getLogger(RentalController.class);

    private final RentalService rentalService;
    private final CustomerService customerService;
    private final ProductInstanceService productInstanceService;

    private static final String RENTALS_PAGE = "rental/rentalsPage";
    private static final String ADD_RENTAL_PAGE = "rental/addRentalPage";
    private static final String SEARCH_RENTAL_PAGE = "rental/searchRentalByIdPage";
    private static final String REDIRECT_RENTALS = "redirect:/rentals";

    @Autowired
    public RentalController(RentalService rentalService, CustomerService customerService, ProductInstanceService productInstanceService) {
        this.rentalService = rentalService;
        this.customerService = customerService;
        this.productInstanceService = productInstanceService;
    }

    @GetMapping("/rentals")
    public String showAllRentals(Model model) {
        List<Rental> rentals = rentalService.getAllRentals();
        model.addAttribute("allRentals", rentals);
        return RENTALS_PAGE;
    }


    @GetMapping("/rentals/add")
    public String showAddRental(Model model) {
        model.addAttribute("allProductInstances", productInstanceService.getAllProductInstances());
        model.addAttribute("allCustomers", customerService.getAllCustomers());
        model.addAttribute("addRentalForm", new AddRentalForm());
        return ADD_RENTAL_PAGE;
    }


    @PostMapping("/rentals/add")
    public String addRental(@Valid @ModelAttribute("addRentalForm") AddRentalForm addRentalForm,
                            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allProductInstances", productInstanceService.getAllProductInstances());
            model.addAttribute("allCustomers", customerService.getAllCustomers());
            return ADD_RENTAL_PAGE;
        }

        try {
            rentalService.createRental(createRentalFromForm(addRentalForm));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create rental: " + e.getMessage());
            model.addAttribute("allProductInstances", productInstanceService.getAllProductInstances());
            model.addAttribute("allCustomers", customerService.getAllCustomers());
            return ADD_RENTAL_PAGE;
        }
        return REDIRECT_RENTALS;
    }

    @PostMapping("/rentals/delete")
    public String deleteRental(@RequestParam int id, Model model) {
        try {
            rentalService.deleteRental(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to delete rental: " + e.getMessage());
        }
        return REDIRECT_RENTALS;
    }

    @GetMapping("/rentals/search")
    public String showSearchRentalPage(Model model) {
        model.addAttribute("getRentalByIdForm", new GetRentalByIdForm());
        return SEARCH_RENTAL_PAGE;
    }

    @PostMapping("/rentals/search")
    public String searchRental(@Valid @ModelAttribute("getRentalByIdForm") GetRentalByIdForm form,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return SEARCH_RENTAL_PAGE;
        }

        try {
            Rental rental = rentalService.getRentalById(form.getId());
            model.addAttribute("allRentals", rental);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to search rental: " + e.getMessage());
        }
        return RENTALS_PAGE;
    }


    private Rental createRentalFromForm(AddRentalForm addRentalForm) {
        Rental rental = new Rental();
        rental.setProductInstance(productInstanceService.getProductInstanceById(addRentalForm.getProductInstanceId()));
        rental.setCustomer(customerService.getCustomerById(addRentalForm.getCustomerId()));
        rental.setRentalDate(addRentalForm.getRentalDate());
        rental.setReturnDate(addRentalForm.getReturnDate());
        return rental;
    }
}






