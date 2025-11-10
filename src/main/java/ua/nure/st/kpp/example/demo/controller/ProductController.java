package ua.nure.st.kpp.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.st.kpp.example.demo.entity.Product;
import ua.nure.st.kpp.example.demo.form.product.AddProductForm;
import ua.nure.st.kpp.example.demo.form.product.GetProductByNameForm;
import ua.nure.st.kpp.example.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProductController {

    Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private static final String PRODUCTS_PAGE = "product/productsPage";
    private static final String EDIT_PRODUCT_PAGE = "product/editProductPage";
    private static final String ADD_PRODUCT_PAGE = "product/addProductPage";
    private static final String SEARCH_PRODUCT_PAGE = "product/searchByNamePage";
    private static final String REDIRECT_PRODUCTS = "redirect:/products";

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("allProducts", products);
        return PRODUCTS_PAGE;
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam int id, Model model) {
        try {
            productService.deleteProduct(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return REDIRECT_PRODUCTS;
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductPage(Model model, @PathVariable int id) {
        Product product = productService.getProductById(id);

        if (product == null) {
            throw new RuntimeException("Product not found" + id);
        }
        model.addAttribute("product", product);
        return EDIT_PRODUCT_PAGE;
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable int id, @Valid @ModelAttribute Product product,
                              BindingResult bindingResult, Model model) {
        product.setId(id);

        if (bindingResult.hasErrors()) {
            return EDIT_PRODUCT_PAGE;
        }
        try {
            productService.updateProduct(product);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update product " + e.getMessage());
            return EDIT_PRODUCT_PAGE;
        }
        return REDIRECT_PRODUCTS;
    }

    @GetMapping("/products/add")
    public String showAddProductPage(Model model) {
        model.addAttribute("addProductForm", new AddProductForm());
        return ADD_PRODUCT_PAGE;
    }

    @PostMapping("products/add")
    public String addProduct(@Valid @ModelAttribute("addProductForm") AddProductForm addProductForm,
                             BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return ADD_PRODUCT_PAGE;
        }
        try {
            productService.createProduct(createProductFromForm(addProductForm));
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add product " + e.getMessage());
            return ADD_PRODUCT_PAGE;
        }
        return REDIRECT_PRODUCTS;
    }

    @GetMapping("/products/search")
    public String showSearchProductPage(Model model) {
        model.addAttribute("getProductByNameForm", new GetProductByNameForm());
        return SEARCH_PRODUCT_PAGE;
    }

    @PostMapping("/products/search")
    public String searchProductsByName(Model model, @ModelAttribute GetProductByNameForm form) {
        List<Product> products = productService.getProductsByName(form.getProductName());

        if (products.isEmpty()) {
            model.addAttribute("errorMessage", "Product not found with name: " + form.getProductName());
        }
        model.addAttribute("allProducts", products);
        return PRODUCTS_PAGE;
    }

    private Product createProductFromForm(AddProductForm addProductForm) {
        return new Product(
                addProductForm.getProductName(),
                addProductForm.getDescription(),
                addProductForm.getPrice()

        );
    }
}
