package ua.nure.st.kpp.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.st.kpp.example.demo.entity.Customer;
import ua.nure.st.kpp.example.demo.form.customer.AddCustomerForm;
import ua.nure.st.kpp.example.demo.form.customer.GetCustomerByLastNameForm;
import ua.nure.st.kpp.example.demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CustomerController {
    Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    private static final String CUSTOMERS_PAGE = "customer/customersPage";
    private static final String EDIT_CUSTOMER_PAGE = "customer/editCustomerPage";
    private static final String ADD_CUSTOMER_PAGE = "customer/addCustomerPage";
    private static final String SEARCH_CUSTOMER_PAGE = "customer/searchByLastNamePage";
    private static final String REDIRECT_CUSTOMERS = "redirect:/customers";

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("allCustomers", customers);
        return CUSTOMERS_PAGE;
    }

    @PostMapping("/customers/delete")
    public String deleteCustomer(@RequestParam int id, Model model) {
        try {
            customerService.deleteCustomer(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return REDIRECT_CUSTOMERS;
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditCustomerPage(@PathVariable int id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        System.out.println(customer);
        model.addAttribute("customer", customer);
        return EDIT_CUSTOMER_PAGE;
    }

    @PostMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable int id, @Valid @ModelAttribute Customer customer,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return EDIT_CUSTOMER_PAGE;
        }
        try {
            customerService.updateCustomer(customer);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update customer: " + e.getMessage());
            return EDIT_CUSTOMER_PAGE;
        }
        return REDIRECT_CUSTOMERS;
    }

    @GetMapping("/customers/add")
    public String showAddCustomerPage(Model model) {
        model.addAttribute("addCustomerForm", new AddCustomerForm());
        return ADD_CUSTOMER_PAGE;
    }

    @PostMapping("/customers/add")
    public String addCustomer(@Valid @ModelAttribute("addCustomerForm") AddCustomerForm addCustomerForm,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return ADD_CUSTOMER_PAGE;
        }
        try {
            customerService.createCustomer(createCustomerFromForm(addCustomerForm));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to add customer: " + e.getMessage());
            return ADD_CUSTOMER_PAGE;
        }
        return REDIRECT_CUSTOMERS;
    }

    @GetMapping("/customers/search")
    public String showSearchCustomerPage(Model model) {
        model.addAttribute("getCustomerByLastNameForm", new GetCustomerByLastNameForm());
        return SEARCH_CUSTOMER_PAGE;
    }

    @PostMapping("/customers/search")
    public String searchCustomersByLastName(Model model, GetCustomerByLastNameForm form) {
        List<Customer> customers = customerService.getCustomersByLastName(form.getLastName());

        if (customers.isEmpty()) {
            model.addAttribute("errorMessage", "Customer not found with last name: " + form.getLastName());
        }
        model.addAttribute("allCustomers", customers);
        return CUSTOMERS_PAGE;
    }

    private Customer createCustomerFromForm(AddCustomerForm addCustomerForm) {
        return new Customer(
                addCustomerForm.getFirstName(),
                addCustomerForm.getLastName(),
                addCustomerForm.getPassportNumber(),
                addCustomerForm.getPhoneNumber()
        );
    }
}
