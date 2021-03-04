package lk.bit.web.business.custom.impl;

import com.google.gson.Gson;
import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.CategoryRepository;
import lk.bit.web.repository.ProductRepository;
import lk.bit.web.repository.SubCategoryRepository;
import lk.bit.web.util.tm.ProductTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
        List<Product> allProducts = productRepository.findAll();
        return null;
    }

    @Override
    public ProductDTO getProduct(String productId) {
        Product p = productRepository.findById(productId).get();
        ProductSubCategory subCategory = subCategoryRepository.findById(p.getSubCategory().getSubCategoryId()).get();
        ProductCategory category = categoryRepository.findById(p.getCategory().getCategoryId()).get();

        return new ProductDTO(p.getProductId(), p.getProductName(), p.getProductDescription(), p.getQuantityPerUnit(),
                p.getQuantityBuyingPrice(), p.getQuantitySellingPrice(), p.getWeight(), p.getDiscountPerUnit(),
                p.getCurrentQuantity(), p.getImageOne(), p.getImageTwo(), p.getImageThree(), p.getStatus(),
                subCategory.getSubCategoryName(), category.getCategoryName());

    }

    @Override
    public void
    saveProduct(List<MultipartFile> imageFiles, String productDetails) {
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
        uploadDir = env.getProperty("static.path") + "product/" + product.getProductId();
        file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
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


    // return a product ID
    @Override
    public String getNewProductId() {
        String productId = "";

//        Product lastProduct = productRepository.findFirstLastProductIdByOrderByProductIdDesc();
        String lastProductId = productRepository.getLastProductId();

//        String lastProductId = lastProduct.getProductId();

        if (lastProductId == null) {
            productId =  "P001";
        } else {
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
            products.add(new ProductTM(p.getProductId(), p.getProductName(), p.getProductCategory(),
                    p.getProductSellingPrice(), p.getProductBuyingPrice(), p.getProductQuantity(),p.getDiscountPerUnit(),p.getOfferStatus(), p.getProductStatus()));
        });
        return products;
    }

    @Override
    public List<ProductTM> getGroupedProductDetails(String categoryName) {
        List<CustomEntity2> category = productRepository.getGroupedProductDetails(categoryName);
        List<ProductTM> products = new ArrayList<>();
        category.forEach(p -> {
            products.add(new ProductTM(p.getProductId(), p.getProductName(), p.getProductCategory(),
                    p.getProductSellingPrice(), p.getProductBuyingPrice(), p.getProductQuantity(),p.getDiscountPerUnit(),
                    p.getOfferStatus(), p.getProductStatus()));
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
        String uploadDir = env.getProperty("static.path") + "product/" + product.getProductId();
        file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
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

    @Override
    public List<ProductDTO> getProductsByCategory(String categoryName) {
        List<Product> productsByCategory = productRepository.getProductsByCategory(categoryName);
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : productsByCategory) {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        }
        return products;
    }

    @Override
    public List<ProductDTO> getActiveLastThreeProducts(String category) {
        String categoryId = subCategoryRepository.getCategoryId(category);
        List<Product> lastActiveThreeProducts = productRepository.getLastActiveThreeProducts(categoryId);
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : lastActiveThreeProducts) {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        }
        return products;
    }

    // save images in locally
    private void setImageAndWriteImage(int i, List<MultipartFile> imageFiles) {
        try {
            imageFiles.get(i).transferTo(new File(file.getAbsolutePath() + "/" + imageFiles.get(i).getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateOfferStatus(String id, int status){
        Product product = productRepository.findById(id).get();
        product.setOfferStatus(status);
        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getOfferedProducts() {
        List<Product> offeredProducts = productRepository.getProductsByOfferStatusEqualsAndStatusEquals(1, "ACTIVE");
        List<ProductDTO> products = new ArrayList<>();
      offeredProducts.forEach(product -> {
          products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                  product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                  product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                  product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                  product.getStatus(), product.getSubCategory().getSubCategoryName(),
                  product.getCategory().getCategoryName()));
      });

      return products;
    }

    @Override
    public List<ProductDTO> getProductsBySearch(String keyword) {
        List<Product> searchProduct = productRepository.getProductBySearch(keyword);
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductsByPriceRange(String minPrice, String maxPrice) {
        List<Product> searchProduct = productRepository.getProductsByPriceRange(new BigDecimal(minPrice), new BigDecimal(maxPrice));
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductBySubCategory(String subCategoryName) {
        List<Product> searchProduct = productRepository.getProductsBySubCategory(subCategoryName);
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductByWeight(String subCategoryName, String weight) {
        List<Product> searchProduct = productRepository.getProductsByWeight(subCategoryName, weight);
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductByQtyPerUnit(String subCategoryName, int qpu) {
        List<Product> searchProduct = productRepository.getProductsByQtyPerUnit(subCategoryName, qpu);
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductsBySubCategoryWithPriceRange(String subCategory, String minPrice, String maxPrice) {
        List<Product> searchProduct = productRepository.getProductsBySubCategoryWithPriceRange(subCategory, new BigDecimal(minPrice), new BigDecimal(maxPrice));
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductByCategoryWithWeight(String categoryName, String weight) {
        List<Product> searchProduct = productRepository.getProductsByCategoryWithWeight(categoryName, weight);
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductByCategoryWithQtyPerUnit(String categoryName, int qpu) {
        List<Product> searchProduct = productRepository.getProductsByCategoryWithQtyPerUnit(categoryName, qpu);
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public List<ProductDTO> getProductsByCategoryWithPriceRange(String categoryName, String minPrice, String maxPrice) {
        List<Product> searchProduct = productRepository.getProductsByCategoryWithPriceRange(categoryName, new BigDecimal(minPrice), new BigDecimal(maxPrice));
        List<ProductDTO> products = new ArrayList<>();
        searchProduct.forEach(product -> {
            products.add(new ProductDTO(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getQuantityPerUnit(), product.getQuantityBuyingPrice(), product.getQuantitySellingPrice(),
                    product.getWeight(), product.getDiscountPerUnit(), product.getCurrentQuantity(),
                    product.getImageOne(), product.getImageTwo(), product.getImageThree(),
                    product.getStatus(), product.getSubCategory().getSubCategoryName(),
                    product.getCategory().getCategoryName()));
        });

        return products;
    }

    @Override
    public boolean existProduct(String productId) {
        return productRepository.existsById(productId);
    }
}
