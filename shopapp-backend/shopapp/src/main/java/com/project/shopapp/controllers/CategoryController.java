package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.reponses.CategoryResponse;
import com.project.shopapp.reponses.UpdateCategoryResponse;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.services.ICategoryService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;


import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    CategoryResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED,errorMessage))
                            .build()
            );
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(
                CategoryResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY)).build()
        );
    }

    @GetMapping("")// http://localhost:8088/api/v1/categories?page=10&limit=2
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable long id,
            @RequestBody @Valid CategoryDTO categoryDTO
    ){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY,id));
    }
}
