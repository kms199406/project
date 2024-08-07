package home.project.service;

import home.project.domain.Product;
import home.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void join(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> findById(Long productId) {
        return Optional.ofNullable(productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(productId + "(으)로 등록된 상품이 없습니다.")));
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findProducts(String brand, String category, String productName, String content, Pageable pageable) {
        Page<Product> productPage = productRepository.findProducts(brand, category, productName, content, pageable);
        if (productPage.getSize() == 0 || productPage.getTotalElements() == 0) {
            throw new IllegalArgumentException("해당하는 상품이 없습니다.");
        }
        return productPage;
    }

    public Page<Product> brandList(Pageable pageable) {
        Page<Product> brandList = productRepository.findAllByOrderByBrandAsc(pageable);
        return brandList;
    }

    public Optional<Product> update(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException(product.getId() + "(으)로 등록된 상품이 없습니다."));

        boolean isModified = false;

        if (product.getBrand() != null && !Objects.equals(existingProduct.getBrand(), product.getBrand())) {
            existingProduct.setBrand(product.getBrand());
            isModified = true;
        }

        if (product.getName() != null && !Objects.equals(existingProduct.getName(), product.getName())) {
            existingProduct.setName(product.getName());
            isModified = true;
        }

        if (product.getSoldQuantity() != null && !Objects.equals(existingProduct.getSoldQuantity(), product.getSoldQuantity())) {
            existingProduct.setSoldQuantity(product.getSoldQuantity());
            isModified = true;
        }

        if (product.getImage() != null && !Objects.equals(existingProduct.getImage(), product.getImage())) {
            existingProduct.setImage(product.getImage());
            isModified = true;
        }

        if (product.getStock() != null && !Objects.equals(existingProduct.getStock(), product.getStock())) {
            if (product.getStock() < 0) {
                throw new DataIntegrityViolationException("재고가 음수일 수 없습니다.");
            }
            existingProduct.setStock(product.getStock());
            isModified = true;
        }

        if (product.getCategory() != null && !Objects.equals(existingProduct.getCategory(), product.getCategory())) {
            existingProduct.setCategory(product.getCategory());
            isModified = true;
        }

        if (!isModified) {
            throw new DataIntegrityViolationException("변경된 상품 정보가 없습니다.");
        }

        return Optional.of(productRepository.save(existingProduct));
    }

    public void deleteById(Long productId) {
        productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException(productId + "(으)로 등록된 상품이 없습니다."));
        productRepository.deleteById(productId);
    }

    public Product increaseStock(Long productId, Long stock) {
        if (stock < 0) {
            throw new DataIntegrityViolationException("재고가 음수일 수 없습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException(productId + "(으)로 등록된 상품이 없습니다."));
        Long currentStock = product.getStock();
        Long newStock = currentStock + stock;
        product.setStock(newStock);
        return productRepository.save(product);
    }

    public Product decreaseStock(Long productId, Long stock) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException(productId + "(으)로 등록된 상품이 없습니다."));
        Long currentStock = product.getStock();
        Long newStock = Math.max(currentStock - stock, 0);
        if (currentStock <= 0 || stock > currentStock) {
            throw new DataIntegrityViolationException("재고가 부족합니다.");
        }
        product.setStock(newStock);
        return productRepository.save(product);
    }


}
