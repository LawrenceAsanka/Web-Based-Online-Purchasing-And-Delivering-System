package lk.bit.web.api;

import lk.bit.web.business.custom.ProductCategoryBO;
import lk.bit.web.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    private ProductCategoryBO categoryBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CategoryDTO> getAllCategories(){
        return categoryBO.getAllCategories();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveCategory(@RequestBody CategoryDTO category){
        if (categoryBO.existCategory(category.getCategoryId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        categoryBO.saveCategory(category);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateCategory(@PathVariable("id") String categoryId, @RequestBody CategoryDTO category){
        if (!categoryBO.existCategory(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        categoryBO.updateCategory(category,categoryId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") String categoryId){
        if (!categoryBO.existCategory(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        categoryBO.deleteCategory(categoryId);
    }
}
