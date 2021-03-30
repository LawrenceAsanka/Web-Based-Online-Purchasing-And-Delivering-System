package lk.bit.web.business.custom.impl;

import com.google.gson.Gson;
import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.CategoryRepository;
import lk.bit.web.repository.ProductRepository;
import lk.bit.web.repository.SubCategoryRepository;
import lk.bit.web.util.email.EmailSender;
import lk.bit.web.util.tm.ProductTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
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
    @Autowired
    private EmailSender emailSender;

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

    //Purchase product mail
    //Every friday 10.30am
/*    @Scheduled(cron = "30 10 ? * FRI")
    protected void autoPurchaseItemFromCompany() {
        List<Product> productList = productRepository.findAll();
        String senderEmail = "asankalorance@gmail.com";
        String subjectMsg = "Supply Goods";
        String productDetailList = "";

        for (Product product : productList) {
            if (product.getStatus().equals("ACTIVE") && product.getCurrentQuantity() < 10) {

                productDetailList += product.getProductName()+"&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+ 20 +"<br>";
                // db eke record ekak save wennath one
            }
        }
        if (!productDetailList.equals("")) {
            emailSender.sendEmail(senderEmail, buildPurchaseEmail(productDetailList), subjectMsg);
        }
    }*/

    @Override
    public boolean existProduct(String productId) {
        return productRepository.existsById(productId);
    }

    private String buildPurchaseEmail(String productList){
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head><meta charset=\"UTF-8\">" +
                "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"><meta name=\"x-apple-disable-message-reformatting\">" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta content=\"telephone=no\" name=\"format-detection\">" +
                "<title>creditor-emai</title> <!--[if (mso 16)]><style type=\"text/css\">     a {text-decoration: none;}     </style><![endif]-->" +
                " <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--> <!--[if gte mso 9]> <![endif]-->" +
                "<style type=\"text/css\">#outlook a {\tpadding:0;}.es-button {\tmso-style-priority:100!important;\ttext-decoration:none!important;}a[x-apple-data-detectors]" +
                " {\tcolor:inherit!important;\ttext-decoration:none!important;\tfont-size:inherit!important;\tfont-family:inherit!important;\tfont-weight:inherit!important;\tline-height:inherit!important;}" +
                ".es-desk-hidden {\tdisplay:none;\tfloat:left;\toverflow:hidden;\twidth:0;\tmax-height:0;\tline-height:0;\tmso-hide:all;}" +
                ".es-button-border:hover a.es-button, .es-button-border:hover button.es-button {\tbackground:#56d66b!important;\tborder-color:#56d66b!important;}" +
                ".es-button-border:hover {\tbackground:#56d66b!important;\tborder-color:#42d159 #42d159 #42d159 #42d159!important;}@media only screen and (max-width:600px)" +
                " {p, ul li, ol li, a { line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120% } h2 " +
                "{ font-size:26px!important; text-align:center; line-height:120% } h3 { font-size:20px!important; text-align:center; line-height:120% } " +
                ".es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a " +
                "{ font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important }" +
                " .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, " +
                ".es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important }" +
                " .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, " +
                ".es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, " +
                ".es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button " +
                "{ font-size:20px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-adaptive table, .es-left, .es-right { width:100%!important }" +
                " .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important }" +
                " .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t " +
                "{ padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important }" +
                " tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important }" +
                " tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important }" +
                " table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }</style></head>\n" +
                "<body style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">" +
                "<div class=\"es-wrapper-color\" style=\"background-color:#F6F6F6\"> <!--[if gte mso 9]><v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\"> <v:fill type=\"tile\" color=\"#f6f6f6\"></v:fill>" +
                " </v:background><![endif]--><table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\">" +
                "<tr><td valign=\"top\" style=\"padding:0;Margin:0\"><table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">" +
                "<tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\">" +
                "<tr><td align=\"left\" style=\"Margin:0;padding-top:10px;padding-bottom:10px;padding-left:20px;padding-right:20px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"><table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:0px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\"><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px;padding-right:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">Dear <strong>Sir / Madam</strong>,<br><br></p>\n" +
                "</td></tr><tr><td align=\"left\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:14px;color:#333333;font-size:14px\">" +
                "Please supply below mention products.<br></p></td>\n" +
                "<br>\n"+
                "</tr><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px;padding-right:5px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;" +
                "font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">"+productList+"\n" +
                "<br>Thank you!<br>VG Distributors,<br>Negombo.</p></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></div></body></html>";
    }
}
