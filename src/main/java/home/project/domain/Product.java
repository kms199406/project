package home.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "product_num", unique = true)
    private String productNum;

    @Check(constraints = "stock >= 0")
    @Column(name = "stock")
    private Long stock;

    @Check(constraints = "sold_Quantity >= 0")
    @Column(name = "sold_Quantity")
    private Long soldQuantity = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code", referencedColumnName = "category_code")
    private Category category;
}