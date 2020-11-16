package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.entity.Product;
import lk.bit.web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ProductBOImpl implements ProductBO {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
       /* List<Product> allProducts = productRepository.findAll();
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : allProducts) {
            products.add(new ProductDTO(
                    product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(),
                    product.getQuantitySellingPrice(), product.getWeight(), product.getDiscountPerUnit(),
                    product.getCurrentQuantity(), imageOne, imageTwo, imageThree,
                    product.getStatus(), product.getSubCategory().getSubCategoryId()));
        }*/
        return null;
    }

    @Override
    public ProductDTO getProduct(String productId) {
        Product product = productRepository.findById(productId).get();
        return null;/*new ProductDTO(
                product.getProductId(), product.getProductName(), product.getProductDescription(),
                product.getQuantityPerUnit(), product.getQuantityBuyingPrice(),
                product.getQuantitySellingPrice(), product.getWeight(), product.getDiscountPerUnit(),
                product.getCurrentQuantity(), imageOne, imageTwo, imageThree,
                product.getStatus(), product.getSubCategory().getSubCategoryId()
        );*/
    }

    @Override
    public void saveProduct(ProductDTO product) throws IOException {

        if (product.getDiscountPerUnit() == null) {
            product.setDiscountPerUnit(new BigDecimal("0.00"));
        }

        if (product.getImageTwo() == null) {
            product.setImageTwo("/media/asanka/0C6A07160C6A0716/Project_Details/BITProject_2020/BIT_final-year-project/src/main/resources/images/noImage.png");
        }

        if (product.getImageThree() == null) {
            product.setImageThree("/media/asanka/0C6A07160C6A0716/Project_Details/BITProject_2020/BIT_final-year-project/src/main/resources/images/noImage.png");
        }

        byte[] imageOne = Files.readAllBytes(Paths.get(product.getImageOne()));
        byte[] imageTwo = Files.readAllBytes(Paths.get(product.getImageTwo()));
        byte[] imageThree = Files.readAllBytes(Paths.get(product.getImageThree()));

        productRepository.save(
                new Product(product.getProductId(), product.getProductName(), product.getProductDescription(),
                        product.getQuantityPerUnit(), product.getQuantityBuyingPrice(),
                        product.getQuantitySellingPrice(), product.getWeight(), product.getDiscountPerUnit(),
                        product.getCurrentQuantity(), imageOne, imageTwo, imageThree,
                        product.getStatus(), product.getProductSubCategoryId())
        );


    }

    @Override
    public boolean existProduct(String productId) {
         return productRepository.existsById(productId);
    }
}
