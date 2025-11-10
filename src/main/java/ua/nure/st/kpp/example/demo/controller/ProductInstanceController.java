package ua.nure.st.kpp.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nure.st.kpp.example.demo.entity.Product;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;
import ua.nure.st.kpp.example.demo.form.productinstance.AddProductInstanceForm;
import ua.nure.st.kpp.example.demo.form.productinstance.GetProductInstanceByNameForm;
import ua.nure.st.kpp.example.demo.service.ProductInstanceService;
import ua.nure.st.kpp.example.demo.service.ProductService;


import javax.validation.Valid;
import java.util.List;

@Controller
public class ProductInstanceController {

    Logger log = LoggerFactory.getLogger(ProductInstanceController.class);

    private final ProductInstanceService productInstanceService;
    private final ProductService productService;
    private static final String PRODUCT_INSTANCES_PAGE = "productInstance/productInstancesPage";
    private static final String ADD_PRODUCT_INSTANCE_PAGE = "productInstance/addProductInstancePage";
    private static final String SEARCH_PRODUCT_INSTANCE_PAGE = "productInstance/searchProductInstanceByNamePage";
    private static final String REDIRECT_PRODUCT_INSTANCES = "redirect:/productInstances";

    @Autowired
    public ProductInstanceController(ProductInstanceService productInstanceService, ProductService productService) {
        this.productInstanceService = productInstanceService;
        this.productService = productService;
    }

    @GetMapping("/productInstances")
    public String showAllProductInstances(Model model) {
        List<ProductInstance> productInstances = productInstanceService.getAllProductInstances();
        model.addAttribute("allProductInstances", productInstances);
        return PRODUCT_INSTANCES_PAGE;
    }

    @GetMapping("/productInstances/add")
    public String showAddProductInstance(Model model) {
        model.addAttribute("allProducts", productService.getAllProducts());
        model.addAttribute("addProductInstanceForm", new AddProductInstanceForm());
        return ADD_PRODUCT_INSTANCE_PAGE;
    }

    @PostMapping("/productInstances/add")
    public String addProductInstance(@Valid @ModelAttribute("addProductInstanceForm") AddProductInstanceForm addProductInstanceForm,
                                     BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return ADD_PRODUCT_INSTANCE_PAGE;
        }

        try {
            productInstanceService.createProductInstance(createProductInstanceFromForm(addProductInstanceForm));
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add product instance: " + e.getMessage());
            model.addAttribute("allProducts", productService.getAllProducts());
            return ADD_PRODUCT_INSTANCE_PAGE;
        }
        return REDIRECT_PRODUCT_INSTANCES;
    }

    @PostMapping("/productInstances/delete")
    public String deleteProductInstance(@RequestParam int id, Model model) {
        try {
            productInstanceService.deleteProductInstance(id);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete product instance: " + e.getMessage());
        }
        return REDIRECT_PRODUCT_INSTANCES;
    }

    @GetMapping("/productInstances/search")
    public String showSearchProductInstancePage(Model model) {
        model.addAttribute("getProductInstanceByNameForm", new GetProductInstanceByNameForm());
        return SEARCH_PRODUCT_INSTANCE_PAGE;
    }

    @PostMapping("/productInstances/search")
    public String searchProductInstanceByName(
            @Valid @ModelAttribute("getProductInstanceByNameForm") GetProductInstanceByNameForm form,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return SEARCH_PRODUCT_INSTANCE_PAGE;
        }

        List<ProductInstance> productInstance;
        try {
            productInstance = productInstanceService.getProductInstanceByName(form.getProductName());
            if (productInstance == null) {
                model.addAttribute("errorMessage", "No product instance found with ID: " + form.getProductName());
                return SEARCH_PRODUCT_INSTANCE_PAGE;
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error occurred: " + e.getMessage());
            return SEARCH_PRODUCT_INSTANCE_PAGE;
        }
        model.addAttribute("allProductInstances", productInstance);
        System.out.println(productInstance);
        return PRODUCT_INSTANCES_PAGE;
    }

    private ProductInstance createProductInstanceFromForm(AddProductInstanceForm addProductInstanceForm) {
        Product product = productService.getProductById(addProductInstanceForm.getProduct().getId());
        ProductInstance productInstance = new ProductInstance();
        productInstance.setProduct(product);
        return productInstance;
    }
}
