package ua.nure.st.kpp.example.demo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.st.kpp.example.demo.entity.Product;
import ua.nure.st.kpp.example.demo.service.ProductService;

@Component
public class StringToProductConverter implements Converter<String, Product> {

    private final ProductService productService;

    public StringToProductConverter(ProductService productService) {this.productService = productService;}

    @Override
    public Product convert(String source) {
        try {
            int id = Integer.parseInt(source);
            return productService.getProductById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product id: " + source, e);
        }
    }
}
