package org.example.vanphong.repository;

import org.example.vanphong.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByCode(String code);
    Optional<Voucher> findByCodeAndIsDelete(String code, Integer isDelete);
    
    // Find active vouchers: not deleted, start date <= now, end date >= now
    // Note: We need to pass current date twice
    @org.springframework.data.jpa.repository.Query("SELECT v FROM Voucher v WHERE v.isDelete = 0 AND v.dateStart <= ?1 AND v.dateEnd >= ?1")
    java.util.List<Voucher> findActiveVouchers(java.util.Date currentDate);
}
