package org.office.repository;

import org.office.model.VoucherByPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherByPriceRepository extends JpaRepository<VoucherByPrice, Integer> {
}
