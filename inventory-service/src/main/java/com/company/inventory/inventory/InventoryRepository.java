package com.company.inventory.inventory;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    // 1. Force Hibernate to issue a "SELECT ... FOR UPDATE" SQL command
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    // 2. Set a timeout hint so threads don't wait indefinitely if a deadlock occurs (e.g., 3000ms)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.product.sku = :sku")
    Optional<Inventory> findByProductSkuWithLock(@Param("sku") String sku);
}