package home.project.controller;


import home.project.domain.*;
import home.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "상품", description = "상품관련 API 입니다")
@RequestMapping(path = "/api/product")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "403", description = "접근이 금지되었습니다.", content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = Product.class)))
})
@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품추가 메서드", description = "상품추가 메서드입니다.")
    @PostMapping("CreateProduct")
    public ResponseEntity<?> createProduct(@RequestBody @Valid  ProductDTOWithoutId productDTOWithoutId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }try {
            Product product = new Product();
            product.setBrand(productDTOWithoutId.getBrand());
            product.setCategory(productDTOWithoutId.getCategory());
            product.setSelledcount(productDTOWithoutId.getSelledcount());
            product.setName(productDTOWithoutId.getName());
            product.setStock(productDTOWithoutId.getStock());
            product.setImage(productDTOWithoutId.getImage());
            productService.join(product);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("상품등록완료", product.getName()+"가 등록되었습니다");
            return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
        }catch (DataIntegrityViolationException e){
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("중복된 값이 입력되었습니다. 해당 상품은 이미 등록되어있습니다", e.getMessage()+"--->위 로그중 Duplicate entry '?'에서 ?는 이미 있는값입니다()");
            return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "전체상품조회 메서드", description = "전체상품조회 메서드입니다.")
    @GetMapping("FindAllProduct")
    public CustomListResponseEntity<Page<Product>> findAllProduct(@PageableDefault(page = 1, size = 5, sort = "id,asc") Pageable pageable) {
            Page<Product> productList = productService.findAll(pageable);
            String successMessage = "전체상품 입니다";
            CustomListResponseBody<Page<Product>> responseBody = new CustomListResponseBody<>(productList.getContent(), successMessage);
            return new CustomListResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "상품명으로 상품조회 메서드", description = "상품명으로 상품조회 메서드입니다.")
    @GetMapping("FindByName")
    public CustomOptionalResponseEntity<Optional<Product>> findProductByName(@RequestParam("productName") String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalStateException("상품명이 입력되지 않았습니다.");
        }
            Optional<Product> productOptional = productService.findByName(productName);
            String successMessage = productName +"로 등록된 상품정보입니다";
            CustomOptionalResponseBody<Optional<Product>> responseBody = new CustomOptionalResponseBody<>(productOptional, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "검색", description = "단순검색 메서드입니다")
    @GetMapping("search")
    public CustomOptionalResponseEntity<Optional<Page<Product>>> search(@RequestParam("contents") String contents, @PageableDefault(page = 1, size = 5, sort = "id,asc") Pageable pageable) {
            Optional<Page<Product>> product = productService.search(contents, pageable);
            String successMessage = contents+"에 해당하는 상품 입니다";
            CustomOptionalResponseBody<Optional<Page<Product>>> responseBody = new CustomOptionalResponseBody<>(product, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "ID로 상품조회 메서드", description = "ID로 상품조회 메서드입니다")
    @GetMapping("FindProductById")
    public CustomOptionalResponseEntity<Optional<Product>> findProductById(@RequestParam("ID") Long ID) {
            Optional<Product> product = productService.findById(ID);
            String successMessage = ID+"에 해당하는 상품 입니다";
            CustomOptionalResponseBody<Optional<Product>> responseBody = new CustomOptionalResponseBody<>(product, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "브랜드명으로 상품조회 메서드", description = "브랜드명으로 상품조회 메서드입니다")
    @GetMapping("FindByBrand")
    public CustomOptionalResponseEntity<Optional<List<Product>>> findProductByBrand(@RequestParam("brand") String brand) {
            Optional<List<Product>> product = productService.findByBrand(brand);
            String successMessage = brand+"에 해당하는 상품 입니다";
            CustomOptionalResponseBody<Optional<List<Product>>> responseBody = new CustomOptionalResponseBody<>(product, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "카테고리로 상품조회 메서드", description = "카테고리로 상품조회 메서드입니다.")
    @GetMapping("FindByCategory")
    public CustomOptionalResponseEntity<Optional<List<Product>>> findProductByCategory(@RequestParam("category") String category) {
            Optional<List<Product>> productCategory = productService.findByCategory(category);
            String successMessage = category+"에 해당하는 상품입니다";
            CustomOptionalResponseBody<Optional<List<Product>>> responseBody = new CustomOptionalResponseBody<>(productCategory, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "상품업데이트(수정) 메서드", description = "상품업데이트(수정) 메서드입니다.")
    @PutMapping("UpdateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Product> productOptional = productService.update(product);
            String successMessage = "상품정보가 수정되었습니다";
            CustomOptionalResponseBody<Optional<Product>> responseBody = new CustomOptionalResponseBody<>(productOptional, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            CustomOptionalResponseBody<Optional<Product>> errorBody = new CustomOptionalResponseBody<>(null, "Validation failed");
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "상품상세 메서드", description = "상품상세 메서드입니다.")
    @GetMapping("DetailProduct")
    public CustomOptionalResponseEntity<Optional<Product>> DetailProduct(@RequestParam("productName") String productName) {
//        switch (productName){
//            case "하의": productName = "10";
//                break;
//            case "상의": productName = "20";
//                break;
//            case "바지": productName = "1010";
//                break;
//            case "티셔츠": productName = "2010";
//                break;
//        }

            Optional<Product> productOptional = productService.findByName(productName);
            String successMessage = productName+"로 등록된 상품 정보입니다";
            CustomOptionalResponseBody<Optional<Product>> responseBody = new CustomOptionalResponseBody<>(productOptional, successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Transactional
    @Operation(summary = "상품삭제 메서드", description = "상품삭제 메서드입니다.")
    @DeleteMapping("DeleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("productName") String productName) {
        try {
            productService.deleteByName(productName);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("상품삭제 완료", productName+"가 삭제되었습니다");
            return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(productName+"로 등록되어있는 상품이 없습니다", e.getMessage());
            return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "전체브랜드조회 메서드", description = "브랜드조회(판매량기준 오름차순정렬) 메서드입니다.")
    @GetMapping("brandList")
    public CustomListResponseEntity<Page<ProductDTOWithBrandId>> brandList(@PageableDefault(page = 1, size = 4, sort = "id,asc") Pageable pageable) {
            Page<ProductDTOWithBrandId> brandListPage = productService.brandList(pageable);
            String successMessage = "전체 브랜드 입니다";
            CustomListResponseBody<Page<ProductDTOWithBrandId>> responseBody = new CustomListResponseBody<>(brandListPage.getContent(), successMessage);
            return new CustomListResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "재고수량 증가 메서드", description = "재고수량 증가 메서드 입니다.")
    @PutMapping ("IncreaseStock")
    public CustomOptionalResponseEntity<Product> increaseStock(@RequestParam("productId") Long productId, @RequestParam("stock") Long stock){
        try {
            Product increaseProduct = productService.increaseStock(productId,stock);
            String successMessage = increaseProduct.getName()+"상품이"+stock+"개 증가하여"+increaseProduct.getStock()+"개가 되었습니다";
            CustomOptionalResponseBody<Product> responseBody = new CustomOptionalResponseBody<>(Optional.ofNullable(increaseProduct), successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            CustomOptionalResponseBody<Product> errorBody = new CustomOptionalResponseBody<>(null, "Validation failed");
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "판매취소(재고수량 증가 메서드)", description = "판매취소(재고수량 증가 메서드 입니다.)")
    @PutMapping ("selledCancle")
    public CustomOptionalResponseEntity<Product> selledCancle(@RequestParam("productId") Long productId, @RequestParam("stock") Long stock){
        try {
            Product increaseProduct = productService.selledCancle(productId,stock);
            String successMessage = increaseProduct.getName()+"상품이"+stock+"개 판매취소되어"+increaseProduct.getStock()+"개가 되었습니다";
            CustomOptionalResponseBody<Product> responseBody = new CustomOptionalResponseBody<>(Optional.ofNullable(increaseProduct), successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            CustomOptionalResponseBody<Product> errorBody = new CustomOptionalResponseBody<>(null, "Validation failed");
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "재고수량 감소 메서드", description = "재고수량 감소 메서드 입니다.")
    @PutMapping("DecreaseStock")
    public CustomOptionalResponseEntity<Product> decreaseStock(@RequestParam("productId") Long productId, @RequestParam("stock") Long stock){
        try {
            Product decreaseProduct = productService.decreaseStock(productId,stock);
            String successMessage = decreaseProduct.getName()+"상품이"+stock+"개 감소하여"+decreaseProduct.getStock()+"개가 되었습니다";
            CustomOptionalResponseBody<Product> responseBody = new CustomOptionalResponseBody<>(Optional.ofNullable(decreaseProduct), successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            CustomOptionalResponseBody<Product> errorBody = new CustomOptionalResponseBody<>(null, "Validation failed");
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "판매완료(재고수량 감소 메서드)", description = "판매완료(재고수량 감소 메서드 입니다.)")
    @PutMapping("SelledProduct")
    public CustomOptionalResponseEntity<Product> selledProduct(@RequestParam("productId") Long productId, @RequestParam("stock") Long stock){
        try {
            Product decreaseProduct = productService.selledProduct(productId,stock);
            String successMessage = decreaseProduct.getName()+"상품이"+stock+"개 판매되어"+decreaseProduct.getStock()+"개가 되었습니다";
            CustomOptionalResponseBody<Product> responseBody = new CustomOptionalResponseBody<>(Optional.ofNullable(decreaseProduct), successMessage);
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            CustomOptionalResponseBody<Product> errorBody = new CustomOptionalResponseBody<>(null, "Validation failed");
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
