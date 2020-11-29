package lk.bit.web.business.custom.impl;

import com.google.gson.Gson;
import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.entity.CustomEntity2;
import lk.bit.web.entity.Product;
import lk.bit.web.entity.ProductCategory;
import lk.bit.web.entity.ProductSubCategory;
import lk.bit.web.repository.CategoryRepository;
import lk.bit.web.repository.ProductRepository;
import lk.bit.web.repository.SubCategoryRepository;
import lk.bit.web.util.ProductTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductBOImpl implements ProductBO {

    private static File file;
    private String uploadDir;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Environment env;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return null;
    }

    @Override
    public ProductDTO getProduct(String productId) {
        Product p = productRepository.findById(productId).get();
        ProductSubCategory subCategory = subCategoryRepository.findById(p.getSubCategory().getSubCategoryId()).get();
        ProductCategory category = categoryRepository.findById(p.getCategory().getCategoryId()).get();

        return new ProductDTO(p.getProductId(),p.getProductName(),p.getProductDescription(),p.getQuantityPerUnit(),
                p.getQuantityBuyingPrice(),p.getQuantitySellingPrice(),p.getWeight(),p.getDiscountPerUnit(),
                p.getCurrentQuantity(),p.getImageOne(),p.getImageTwo(),p.getImageThree(),p.getStatus(),
                subCategory.getSubCategoryName(),category.getCategoryName());

    }

    @Override
    public void saveProduct(List<MultipartFile> imageFiles, String productDetails) {
        String firstImagePath = "";
        String secondImagePath = "";
        String thirdImagePath = "";

        // create Gson object
        Gson gson = new Gson();
        ProductDTO product = gson.fromJson(productDetails, ProductDTO.class);


        // get category and subcategory name
        String categoryId = subCategoryRepository.getCategoryId(product.getProductCategory());
        int subCategoryId = subCategoryRepository.getSubCategoryId(product.getProductSubCategory(),
                product.getProductCategory());

        // check whether folder is exist or not
        uploadDir = env.getProperty("static.path") + product.getProductId();
        file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdir();
        }

        // check multipart file and set image path
        for (int i = 0; i < imageFiles.size(); i++) {
            switch (i) {
                case 1:
                    secondImagePath = imageFiles.get(i).getOriginalFilename();
                    setImageAndWriteImage(i, imageFiles);
                    break;
                case 2:
                    thirdImagePath = imageFiles.get(i).getOriginalFilename();
                    setImageAndWriteImage(i, imageFiles);
                    break;
                default:
                    firstImagePath = imageFiles.get(i).getOriginalFilename();
                    setImageAndWriteImage(i, imageFiles);
                    break;
            }
        }


        //save data in database
        productRepository.save(new Product(
                product.getProductId(), product.getProductName(), product.getProductDescription(), product.getQuantityPerUnit(),
                product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(), product.getWeight(),
                product.getDiscountPerUnit(), product.getCurrentQuantity(), firstImagePath, secondImagePath,
                thirdImagePath, "ACTIVE", subCategoryId, categoryId
        ));

    }

    @Override
    public boolean existProduct(String productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public String getNewProductId() throws Exception {
        String productId = "";

        Product lastProduct = productRepository.findFirstLastProductIdByOrderByProductIdDesc();

        String lastProductId = lastProduct.getProductId();

        if (lastProductId == null) {
            return "P001";
        }else{
            String id = lastProductId.replace("P", "");
            int newProductId = Integer.parseInt(id) + 1;
            if (newProductId < 10) {
                productId = "P00" + newProductId;
            } else if (newProductId < 100) {
                productId = "P0" + newProductId;
            } else {
                productId = "P" + newProductId;
            }
        }
        return productId;
    }

    @Override
    public List<ProductTM> getAllProductsDetails() {
        List<CustomEntity2> allProducts = productRepository.getAllProducts();
        List<ProductTM> products = new ArrayList<>();
        allProducts.forEach(p -> {
            products.add(new ProductTM(p.getProductId(),p.getProductName(),p.getProductCategory(),
                    p.getProductSellingPrice(),p.getProductBuyingPrice(),p.getProductQuantity(),p.getProductStatus()));
        });
        return products;
    }

    @Override
    public List<ProductTM> getGroupedProductDetails(String categoryName) {
        List<CustomEntity2> category = productRepository.getGroupedProductDetails(categoryName);
        List<ProductTM> products = new ArrayList<>();
        category.forEach(p -> {
            products.add(new ProductTM(p.getProductId(),p.getProductName(),p.getProductCategory(),
                    p.getProductSellingPrice(),p.getProductBuyingPrice(),p.getProductQuantity(),p.getProductStatus()));
        });
        return products;
    }

    @Override
    public void updateStatus(String status, String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(status);
            productRepository.save(product);
        }
    }

    @Override
    public void updateProduct(List<MultipartFile> imageFiles, String productDetails, String status, String productId) {
        String firstImagePath = "";
        String secondImagePath = "";
        String thirdImagePath = "";

        // create Gson object
        Gson gson = new Gson();
        ProductDTO product = gson.fromJson(productDetails, ProductDTO.class);
        Product p = productRepository.findById(product.getProductId()).get();

        // check whether folder is exist or not
        String uploadDir = env.getProperty("static.path") + product.getProductId();
        file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdir();
        }

     /*   uploadDir = env.getProperty("static.path") + product.getProductId();
        File folder = new File(uploadDir);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            System.out.println(file);
        }*/

        if (imageFiles.size() > 0) {
            // check multipart file and set image path
            for (int i = 0; i < imageFiles.size(); i++) {
                switch (i) {
                    case 1:
                        secondImagePath = imageFiles.get(i).getOriginalFilename();
                        setImageAndWriteImage(i, imageFiles);
                        p.setImageTwo(secondImagePath);
                        break;
                    case 2:
                        thirdImagePath = imageFiles.get(i).getOriginalFilename();
                        setImageAndWriteImage(i, imageFiles);
                        p.setImageThree(thirdImagePath);
                        break;
                    default:
                        firstImagePath = imageFiles.get(i).getOriginalFilename();
                        setImageAndWriteImage(i, imageFiles);
                        p.setImageOne(firstImagePath);
                        break;
                }
            }
        }
            // get category and subcategory name
            String categoryId = subCategoryRepository.getCategoryId(product.getProductCategory());
            int subCategoryId = subCategoryRepository.getSubCategoryId(product.getProductSubCategory(),
                    product.getProductCategory());

            p.setProductId(productId);
            p.setProductName(product.getProductName());
            p.setProductDescription(product.getProductDescription());
            p.setCategory(new ProductCategory(categoryId));
            p.setSubCategory(new ProductSubCategory(subCategoryId));
            p.setQuantityBuyingPrice(product.getQuantityBuyingPrice());
            p.setQuantitySellingPrice(product.getQuantitySellingPrice());
            p.setCurrentQuantity(product.getCurrentQuantity());
            p.setQuantityPerUnit(product.getQuantityPerUnit());
            p.setWeight(product.getWeight());
            p.setDiscountPerUnit(product.getDiscountPerUnit());
            p.setStatus(status);

            productRepository.save(p);

    }

    // save images in locally
    private void setImageAndWriteImage(int i, List<MultipartFile> imageFiles) {
        try {
            imageFiles.get(i).transferTo(new File(file.getAbsolutePath() + "/" + imageFiles.get(i).getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}