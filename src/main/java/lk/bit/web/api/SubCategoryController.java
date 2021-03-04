package lk.bit.web.api;

import lk.bit.web.business.custom.ProductSubCategoryBO;
import lk.bit.web.dto.SubCategoryDTO;
import lk.bit.web.util.tm.SubCategoryTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/subcategories")
public class SubCategoryController {

    @Autowired
    private ProductSubCategoryBO subCategoryBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public List<SubCategoryTM> getAllSubCategories() {
        List<SubCategoryDTO> allSubCategories = subCategoryBO.getAllSubCategories();
        List<SubCategoryTM> subCategories = new ArrayList<>();
        for (SubCategoryDTO subCategory : allSubCategories) {
            subCategories.add(new SubCategoryTM(subCategory.getSubCategoryId(), subCategory.getSubCategoryName()));
        }
        return subCategories;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public String getSubCategory(@PathVariable int id) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return subCategoryBO.getSubCategory(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<SubCategoryDTO> getSubCategory(@RequestParam("sort") String categoryName) {
        if (categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return subCategoryBO.getSortedSubCategories(categoryName);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveSubCategory(@RequestParam("categoryName") String categoryName,
                                @RequestBody SubCategoryDTO subCategory) {
        if (categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        subCategoryBO.saveSubCategory(subCategory.getSubCategoryName(), categoryName);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateSubCategory(@PathVariable("id") int subCategoryId,
                                  @RequestBody SubCategoryDTO subCategory){
        if (!subCategoryBO.subCategoryExist(subCategoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
       subCategoryBO.updateSubCategory(subCategory,subCategoryId);
    }

/*    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteSubCategory(@PathVariable("id") String subCategoryId) {
        if (!subCategoryBO.subCategoryExist(subCategoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        subCategoryBO.deleteSubCategory(subCategoryId);
    }*/

}
