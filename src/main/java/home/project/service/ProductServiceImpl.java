package home.project.service;

import home.project.domain.Product;
import home.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    public void join (Product product){

        productRepository.save(product);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findProductsByName(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable);
    }



    public Optional<Product> findById(Long ID) {
        Product product = productRepository.findById(ID).orElseThrow(() -> { throw new IllegalArgumentException(ID+"로 설정된 상품이 없습니다."); });
        return Optional.ofNullable(product);
    }

    public Page<Product> findProducts(String brand, String category, String productName, String query, Pageable pageable) {
        Page<Product> productPage = productRepository.findProducts(brand ,category, productName, query, pageable);
        if (productPage.getSize()==0||productPage.getTotalElements()==0) {
            throw new IllegalArgumentException("해당하는 제품을 찾을 수 없습니다.");
        }
        return productRepository.findProducts(brand, category, productName, query, pageable);

    }

    public Page<Product> search(String contents, Pageable pageable) {
        String category = "";

    if (contents.contains("셔츠")) {
        if (contents.contains("티")) {
            if (contents.contains("반팔")) {
                category = "010101";
            } else if (contents.contains("긴팔")) {
                category = "010102";
            } else {
                category = "0101";
            }
        } else if (contents.contains("블라우스")) {
            if (contents.contains("반팔")) {
                category = "010201";
            } else if (contents.contains("긴팔")) {
                category = "010202";
            }else{
                category = "0102";
            }
        } else if (contents.contains("와이셔츠")) {
            if (contents.contains("반팔")) {
                category = "010201";
            } else if (contents.contains("긴팔")) {
                category = "010202";
            }else{
                category = "0102";
            }
        } else if (contents.contains("반팔")) {
            category = "010201";
        } else if (contents.contains("긴팔")) {
            category = "010202";
        } else {
            category = "0102";
        }
    } else if (contents.contains("니트")) {
        if (contents.contains("반팔")) {
            category = "010301";
        } else if (contents.contains("긴팔")) {
            category = "010302";
        }else{
            category = "0103";
        }
    } else if (contents.contains("바지")) {
        if (contents.contains("면")) {
            category = "0201";
        } else if (contents.contains("청")) {
            category = "0203";
        } else {
            category = "02";
            }
        }
    if (category.isEmpty()) {throw new IllegalArgumentException(contents + "에 해당하는 상품을 찾을수 없습니다.");}
    Page<Product> product = productRepository.search(contents, category, pageable);
        return new PageImpl<>(product.getContent(), pageable, product.getTotalElements());
    }

//    public Optional<List<Product>> search(String contents) {
//        String category = "";
//
//        switch(contents) {
//            case "%셔츠%":
//                category = searchDTO.handleShirts(contents);
//                break;
//            case "%니트%":
//                category = searchDTO.handleKnit(contents);
//                break;
//            case "%바지%":
//                category = searchDTO.handlePants(contents);
//                break;
//        }
//        List<Product> product = productRepository.search(contents,category).filter(prducts -> !prducts.isEmpty()).orElseThrow(() -> { throw new IllegalArgumentException(contents+"상품을 찾을수 없습니다."); });
//        return Optional.ofNullable(product);
//    }

    public Page<Product> findByBrand(String brand, Pageable pageable) {
        Page<Product> productPage = productRepository.findByBrand(brand, pageable);
        if (productPage.getSize()==0||productPage.getTotalElements()==0) {throw new IllegalArgumentException(brand + " 브랜드를 찾을 수 없습니다.");}
        return productPage;
    }


    public Page<Product> findByCategory(String category, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        if (productPage.getSize()==0||productPage.getTotalElements()==0) {throw new IllegalArgumentException(category + " 카테고리에 상품이 없습니다.");}
        return productPage;
    }


//    public Optional<Product> DetailProduct (Long productid){
//        Product existProduct = productRepository.findById(productid).orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
//        return Optional.ofNullable(existProduct);
//    }

    public Optional<Product> update (Product product){
        Product exsitsProduct = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        exsitsProduct.setBrand(product.getBrand());
        exsitsProduct.setName(product.getName());
        exsitsProduct.setSelledcount(product.getSelledcount());
        exsitsProduct.setImage(product.getImage());
        exsitsProduct.setStock(product.getStock());
        exsitsProduct.setCategory(product.getCategory());
        Long currentStock = product.getStock();
        if (currentStock < 0 || exsitsProduct.getStock() > currentStock){throw new DataIntegrityViolationException("재고가 음수 일 수 없습니다.");}
        productRepository.save(exsitsProduct);

        Optional<Product> newProduct = productRepository.findById(exsitsProduct.getId());
        return newProduct;
    }

    public void deleteById (Long productId){
        productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        productRepository.deleteById(productId);
    }

    public Page<Product> brandList(Pageable pageable){
        Page<Product> brandList = productRepository.findAllByOrderByBrandAsc(pageable);
        return brandList;
    }

    public Product increaseStock(Long productId , Long stock) {
        if (stock < 0) {
            throw new DataIntegrityViolationException("재고 수량은 음수일 수 없습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        Long currentStock = product.getStock();
        Long newStock = currentStock + stock;
        product.setStock(newStock);
        return productRepository.save(product);
    }

    public Product selledCancle(Long productId, Long stock) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        if (stock < 0) {
            throw new DataIntegrityViolationException("재고 수량은 음수일 수 없습니다.");
        }
        Long currentStock = product.getStock();
        Long newStock = Math.max(currentStock + stock, 0);
        Long diffStock = newStock - currentStock;
        product.setStock(newStock);
        Long currentSelledCount = product.getSelledcount();
        Long newSelledCount = Math.max(currentSelledCount - diffStock, 0);
        product.setSelledcount(newSelledCount);
        return productRepository.save(product);
    }

    public Product decreaseStock(Long productId , Long stock) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        Long currentStock = product.getStock();
        Long newStock = Math.max(currentStock - stock, 0);
        if (currentStock <= 0 || stock > currentStock) {throw new DataIntegrityViolationException("재고가 부족합니다.");}
        product.setStock(newStock);
        return productRepository.save(product);
    }

    public Product selledProduct(Long productId, Long stock) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        Long currentStock = product.getStock();

        if (currentStock == 0) { throw new DataIntegrityViolationException("재고가 없어 상품을 판매할 수 없습니다.");}
        if (currentStock <= 0 || stock > currentStock) { throw new DataIntegrityViolationException("재고가 부족합니다.");}

        Long newStock = currentStock - stock;
        product.setStock(newStock);

        Long soldCount = product.getSelledcount();
        product.setSelledcount(soldCount + stock);

        return productRepository.save(product);
        }
    }
