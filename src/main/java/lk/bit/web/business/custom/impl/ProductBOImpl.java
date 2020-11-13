package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.product_dto.ProductDTO;
import lk.bit.web.entity.product_entity.Product;
import lk.bit.web.repository.product_repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ProductBOImpl implements ProductBO {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveProduct(ProductDTO product) throws IOException {

        if (product.getDiscountPerUnit() == null) {
            product.setDiscountPerUnit(new BigDecimal("0.00"));
        }

        if (product.getImageTwo() == null) {
            product.setImageTwo("");
        }

        if (product.getImageThree() == null) {
            product.setImageThree("");
        }

        byte[] imageOne = Files.readAllBytes(Paths.get(product.getImageOne()));
        byte[] imageTwo = Files.readAllBytes(Paths.get(product.getImageTwo()));
        byte[] imageThree = Files.readAllBytes(Paths.get(product.getImageThree()));

        productRepository.save(
                new Product(product.getProductId(), product.getProductName(), product.getProductDescription(),
                        product.getQuantityPerUnit(), product.getQuantityBuyingPrice(),
                        product.getQuantitySellingPrice(), product.getWeight(), product.getDiscountPerUnit(),
                        product.getCurrentUnitCount(), imageOne, imageTwo, imageThree,
                        product.getStatus(), product.getProductSubCategoryId())
        );


    }
}
