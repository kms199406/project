package home.project.repository;

import home.project.domain.Orders;
import home.project.domain.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Long>, ShippingRepositoryCustom  {

//    Page<Shipping> findByMemberId(Long memberId, Pageable pageable);

}