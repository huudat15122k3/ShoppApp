package com.project.shopapp.controllers;



import com.github.javafaker.Faker;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.reponses.ProductListResponse;
import com.project.shopapp.reponses.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.services.ProductService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private  final ProductService productService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("")// http://localhost:8088/api/v1/products
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : 0, limit, Sort.by("id").ascending());

        Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
        // Lấy tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")// http://localhost:8088/api/v1/products
    public ResponseEntity<?> getProductsById(@PathVariable int id){
        try {
            Product existingProduct = productService.getProductById(id);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        //eg: 1,3,5,7
        try {
            // Tách chuỗi ids thành một mảng các số nguyên
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct =productService.createProduct(productDTO);



            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id")Long productId,
            @RequestParam("files") List<MultipartFile> files
    ){
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body(
                        localizationUtils
                                .getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX_5)
                );
            }
            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file : files){
                if(file!=null){
                    if(file.getSize() == 0) continue;
                    if(file.getSize() > 10 * 1024 * 1024){ // Kích thước lớn hơn 10MB
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                                localizationUtils
                                        .getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE)
                        );
                    }
                    String contentType = file.getContentType();
                    if(!contentType.startsWith("image/")){
                        return  ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                                localizationUtils
                                        .getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE)
                        );
                    }
                    //Lữu và cập nhập thumbnail trong DTO
                    String fileName = storeFile(file);
                    //Lưu vào product_images
                    ProductImage productImage = productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .imageUrl(fileName)
                                    .build()
                    );
                    productImages.add(productImage);
                }
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private  String storeFile(MultipartFile file) throws IOException{
        if(!isImageFile(file)){
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //Thêm UUID đảm bảo file unique
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        //Đường dẫn đến thư mục lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        //Kiểm tra và tạo thư mục nếu không tồn tại
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //Đường dẫn đầy đủ đến file
        java.nio.file.Path destination  = Paths.get(uploadDir.toString(),uniqueFileName);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    private  boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.startsWith("image/");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDTO productDTO
    ){
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {
           return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product successfully!");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for(int i=0; i< 1000000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,90000000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,4))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }

}

