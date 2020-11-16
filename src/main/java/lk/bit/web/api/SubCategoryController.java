package lk.bit.web.api;

import lk.bit.web.business.custom.ProductSubCategoryBO;
import lk.bit.web.dto.SubCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/subcategories")
public class SubCategoryController {

    @Autowired
    private ProductSubCategoryBO subCategoryBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<SubCategoryDTO> getAllSubCategories(){
        return subCategoryBO.getAllSubCategories();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveSubCategory(@RequestBody SubCategoryDTO subCategory){
        if (subCategoryBO.subCategoryExist(subCategory.getSubCategoryId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        subCategoryBO.saveSubCategory(subCategory);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void saveSubCategory(@PathVariable("id") String subCategoryId,
                                  @RequestBody SubCategoryDTO subCategory){
        if (!subCategoryBO.subCategoryExist(subCategoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        subCategoryBO.updateSubCategory(subCategory.getSubCategoryName(),subCategory.getStatus(),
                subCategory.getCategoryId(),subCategoryId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteSubCategory(@PathVariable("id") String subCategoryId){
        if (!subCategoryBO.subCategoryExist(subCategoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        subCategoryBO.deleteSubCategory(subCategoryId);
    }

}
