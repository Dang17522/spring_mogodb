package com.zalo.Spring_Zalo.Controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import com.zalo.Spring_Zalo.Entities.Category;
import com.zalo.Spring_Zalo.Repo.CategoryMongoRepo;
import com.zalo.Spring_Zalo.Response.ApiDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zalo.Spring_Zalo.DTO.ProductDto;
import com.zalo.Spring_Zalo.Entities.Product;
import com.zalo.Spring_Zalo.Exception.ApiNotFoundException;
import com.zalo.Spring_Zalo.Repo.ProductMongoRepo;
import com.zalo.Spring_Zalo.Response.ApiResponse;
import com.zalo.Spring_Zalo.Service.CloudinaryService;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;

@RestController
    @RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductMongoRepo productRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private CategoryMongoRepo categoryRepo;

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam(value = "key",required = false, defaultValue = "" ) String key,
                                            @RequestParam(value = "sort",required = false, defaultValue ="descend") String sort,
                                            @RequestParam(value = "pageSize" ,defaultValue = "5") int pageSize,
                                            @RequestParam(value = "pageNumber" ,defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort.equals("ascend") ? Sort.by("createAt").ascending() : Sort.by("createAt").descending());
        Page<Product> productsPage = productRepo.findAllByKey(key,pageable);
        Page<ProductDto> map = productsPage.map(this::mapToProductDto);
        ApiDataResponse apiResponse = new ApiDataResponse("Get all products success !!!", true, 200, map);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                 @RequestPart("name") String name,
                                                 @RequestPart("status") String status,
                                                 @RequestPart("quantity") String quantity,
                                                 @RequestPart("category") String categoryId,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ApiNotFoundException("Not found product withh id: " + id));
        if(product.getCategory().getId() != Integer.parseInt(categoryId)) {
            Category category = categoryRepo.findById(Integer.parseInt(categoryId)).orElseThrow(() -> new ApiNotFoundException("Not found category withh id: " + categoryId));
            product.setCategory(category);
        }
        product.setName(name);
        product.setStatus(Integer.parseInt(status));
        product.setQuantity(Integer.parseInt(quantity));
        product.setUpdatedAt(LocalDateTime.now());
        if (file != null) {
            cloudinaryService.deleteImageUpload(product.getPublicId());

            Map data = cloudinaryService.upload(file);
            product.setImage(String.valueOf(data.get("secure_url")));
            product.setPublicId(String.valueOf(data.get("public_id")));

        }

        Product savedProduct = productRepo.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Product> updateStatusProduct(@PathVariable Integer id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Not found product withh id: " + id));
        product.setStatus(0);
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepo.save(product); // Cập nhật thông tin của sản phẩm
        return ResponseEntity.ok(savedProduct); // Trả về sản phẩm đã được cập nhật
    }

    @PostMapping(value = "/")
    public ResponseEntity<Product> createProduct(@RequestPart("name") String name,
                                                @RequestPart("status") String status,
                                                @RequestPart("quantity") String quantity,
                                                @RequestPart("category") String categoryId,
                                                @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        Product product = new Product();
        // String fileName = saveFile(file);
        Category category = categoryRepo.findById(Integer.parseInt(categoryId)).orElseThrow(() -> new ApiNotFoundException("Not found category withh id: " + categoryId));
        if (file != null) {
            Map data = cloudinaryService.upload(file);
            product.setImage(String.valueOf(data.get("secure_url")));
            product.setPublicId(String.valueOf(data.get("public_id")));
        }
        product.setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        product.setName(name);
        product.setQuantity(Integer.parseInt(quantity));
        product.setVote(0);
        product.setStatus(Integer.parseInt(status));
        product.setCreateAt(LocalDateTime.now());
        product.setCategory(category);

        Product pro = productRepo.save(product);
        return new ResponseEntity<>(pro, HttpStatus.CREATED);
    }



    public String saveFile(MultipartFile file) throws IOException {
        File directory = new File("images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());

        String uploadDir = System.getProperty("user.dir") + "/images/";
        File destination = new File(uploadDir + fileName);

        file.transferTo(destination);

        return fileName;
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ApiNotFoundException("Product not found with id: " + productId));
        product.setDeletedAt(LocalDateTime.now());
        productRepo.save(product);
        // cloudinaryService.deleteImageUpload(product.getPublicId());
        ApiResponse apiResponse = new ApiResponse("Delete product success", true, 200);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private ProductDto mapToProductDto(Product product) {
        ResourceBundle infomation = ResourceBundle.getBundle("application");
        String pathImage = infomation.getString("path_image");
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setStatus(product.getStatus());
        productDto.setImage( product.getImage());
        productDto.setPublicId(product.getPublicId());
        productDto.setCreateAt(product.getCreateAt());
        productDto.setVote(product.getVote());
        productDto.setQuantity(product.getQuantity());
        productDto.setCategory(product.getCategory().getId());
        return productDto;
    }

}
