package org.example.vanphong.repository;

import org.example.vanphong.model.VoucherByPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherByPriceRepository extends JpaRepository<VoucherByPrice, Integer> {
}
