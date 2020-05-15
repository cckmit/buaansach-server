package vn.com.buaansach.web.customer_care.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;
import vn.com.buaansach.web.customer_care.service.dto.CustomerCareCustomerVoucherCodeDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCareCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findOneByCustomerPhone(String customerPhone);

    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.CustomerCareCustomerVoucherCodeDTO(customer, voucherCode) " +
            "FROM CustomerEntity customer " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.VoucherCodeEntity voucherCode " +
            "ON customer.customerPhone = voucherCode.customerPhone " +
            "WHERE voucherCode.voucherGuid = :voucherGuid " +
            "AND customer.customerPhone LIKE %:search%")
    Page<CustomerCareCustomerVoucherCodeDTO> findPageManagerCustomerCodeDTO(@Param("voucherGuid") UUID voucherGuid, @Param("search") String search, Pageable pageable);

    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.CustomerCareCustomerVoucherCodeDTO(customer, voucherCode) " +
            "FROM CustomerEntity customer " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.VoucherCodeEntity voucherCode " +
            "ON customer.customerPhone = voucherCode.customerPhone " +
            "WHERE voucherCode.voucherGuid = :voucherGuid " +
            "AND voucherCode.voucherCodeSentStatus = :voucherCodeSentStatus " +
            "ORDER BY customer.createdDate ASC")
    List<CustomerCareCustomerVoucherCodeDTO> findUnsentVoucher(@Param("voucherGuid") UUID voucherGuid,
                                                               @Param("voucherCodeSentStatus") VoucherCodeSentStatus voucherCodeSentStatus);
}
