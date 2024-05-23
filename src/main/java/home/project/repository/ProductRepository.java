package home.project.repository;

import home.project.domain.Product;
import home.project.domain.ProductDTOWithBrandId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository  extends JpaRepository<Product, Long> {

//    @Query("SELECT p.name, p.brand, p.image FROM Product p ORDER BY p.selledcount DESC")
//    List<String> findTop5ByOrderBySelledcountDesc();
@Query("SELECT DISTINCT new home.project.domain.ProductDTOWithBrandId(p.brand, MAX(p.id)) FROM Product p GROUP BY p.brand ORDER BY p.brand ASC")
    Page<ProductDTOWithBrandId> findAllByOrderByBrandAsc(Pageable pageable);

    Optional<Product> findByName(String productName);

    Optional<Product> findById(Long ID);

    void deleteByName(String productName);

    @Query("SELECT DISTINCT p FROM Product p WHERE p.name LIKE %:contents% OR p.brand LIKE %:contents% OR p.category LIKE %:category%")
    Optional<List<Product>> search(@Param("contents")String contents, @Param("category")String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category LIKE CONCAT(:category, '%')")
    Optional<List<Product>> findByCategory(@Param("category")String category);

    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    Optional<List<Product>> findByBrand(@Param("brand")String brand);
}
