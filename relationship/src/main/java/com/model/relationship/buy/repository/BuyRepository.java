package com.model.relationship.buy.repository;

import com.model.relationship.buy.model.BuyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyRepository extends JpaRepository<BuyProduct, Long> {
}
