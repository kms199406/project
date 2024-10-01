package home.project.repository;


import home.project.domain.Category;
import home.project.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.brand FROM Product p ORDER BY p.brand ASC")
    Page<Product> findAllByOrderByBrandAsc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:brand IS NULL OR :brand = '' OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:categoryCode IS NULL OR :categoryCode = '' OR LOWER(p.category.code) LIKE LOWER(CONCAT('%', :categoryCode, '%'))) AND " +
            "(:productName IS NULL OR :productName = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
            "(:content IS NULL OR :content = '' OR " +
            "LOWER(CONCAT(p.brand, ' ', p.category.code, ' ', p.name)) LIKE LOWER(CONCAT('%', :content, '%')))")
    Page<Product> findProducts(@Param("brand") String brand,
                               @Param("categoryCode") String categoryCode,
                               @Param("productName") String productName,
                               @Param("content") String content,
                               Pageable pageable);

    boolean existsByProductNum(String productNum);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByCategoryCodeStartingWith(String category);



}
