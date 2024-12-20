package com.zalo.Spring_Zalo.Controller;

import com.zalo.Spring_Zalo.DTO.CategoryDto;
import com.zalo.Spring_Zalo.Entities.Category;
import com.zalo.Spring_Zalo.Exception.ApiNotFoundException;
import com.zalo.Spring_Zalo.Repo.CategoryMongoRepo;
import com.zalo.Spring_Zalo.Response.ApiDataResponse;
import com.zalo.Spring_Zalo.Response.ApiResponse;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryMongoRepo categoryRepo;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "pageSize" ,defaultValue = "5") int pageSize,
                                              @RequestParam(value = "pageNumber" ,defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        Page<CategoryDto> map = categoryPage.map(this::mapToCategoryDto);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = Category.builder()
                .id(sequenceGeneratorService.generateSequence(Category.SEQUENCE_NAME))
                .name(categoryDto.getName())
                .status(categoryDto.getStatus())
                .createAt(LocalDateTime.now())
                .build();
        Category c = categoryRepo.save(category);
        ApiDataResponse apiResponse = new ApiDataResponse("Create category successfully",true,200, c);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId,
                                             @RequestBody CategoryDto categoryDto) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ApiNotFoundException("Category not found with id: " + categoryId));
        category.setName(categoryDto.getName());
        category.setStatus(categoryDto.getStatus());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepo.save(category);
        ApiResponse apiResponse = new ApiResponse("Update category successfully",true,200, category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ApiNotFoundException("Category not found with id: " + categoryId));
        categoryRepo.delete(category);
        ApiResponse apiResponse = new ApiResponse("Delete category successfully",true,200, category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .status(category.getStatus())
                .createAt(category.getCreateAt())
                .build();
    }
}
