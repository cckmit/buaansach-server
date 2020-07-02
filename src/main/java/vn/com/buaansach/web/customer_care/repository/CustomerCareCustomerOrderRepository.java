package vn.com.buaansach.web.customer_care.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerOrderEntity;
import vn.com.buaansach.entity.enumeration.CustomerCareStatus;
import vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCareCustomerOrderRepository extends JpaRepository<CustomerOrderEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO(customerOrder, customer, store) " +
            "FROM CustomerOrderEntity customerOrder " +
            "JOIN vn.com.buaansach.entity.customer.CustomerEntity customer " +
            "ON customerOrder.customerPhone = customer.customerPhone " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON customerOrder.storeGuid = store.guid " +
            "WHERE customerOrder.customerPhone LIKE %:customerPhone% " +
            "AND customerOrder.createdDate >= :startDate " +
            "AND customerOrder.createdDate <= :endDate " +
            "AND customerOrder.customerCareStatus = :customerCareStatus " +
            "AND customerOrder.orderCount >= :orderCountMin " +
            "AND customerOrder.orderCount <= :orderCountMax " +
            "ORDER BY customerOrder.createdDate ASC")
    List<CustomerCareCustomerOrderDTO> findListCustomerOrder(@Param("customerPhone") String customerPhone,
                                                             @Param("startDate") Instant startDate,
                                                             @Param("endDate") Instant endDate,
                                                             @Param("customerCareStatus") CustomerCareStatus customerCareStatus,
                                                             @Param("orderCountMin") int orderCountMin,
                                                             @Param("orderCountMax") int orderCountMax);

    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO(customerOrder, customer, store) " +
            "FROM CustomerOrderEntity customerOrder " +
            "JOIN vn.com.buaansach.entity.customer.CustomerEntity customer " +
            "ON customerOrder.customerPhone = customer.customerPhone " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON customerOrder.storeGuid = store.guid " +
            "WHERE customerOrder.customerPhone LIKE %:customerPhone% " +
            "AND customerOrder.createdDate >= :startDate " +
            "AND customerOrder.createdDate <= :endDate " +
            "AND customerOrder.customerCareStatus = :customerCareStatus " +
            "AND customerOrder.orderCount >= :orderCountMin " +
            "ORDER BY customerOrder.createdDate ASC")
    List<CustomerCareCustomerOrderDTO> findListCustomerOrder(@Param("customerPhone") String customerPhone,
                                                             @Param("startDate") Instant startDate,
                                                             @Param("endDate") Instant endDate,
                                                             @Param("customerCareStatus") CustomerCareStatus customerCareStatus,
                                                             @Param("orderCountMin") int orderCountMin);

    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO(customerOrder, customer, store) " +
            "FROM CustomerOrderEntity customerOrder " +
            "JOIN vn.com.buaansach.entity.customer.CustomerEntity customer " +
            "ON customerOrder.customerPhone = customer.customerPhone " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON customerOrder.storeGuid = store.guid " +
            "WHERE customerOrder.customerPhone LIKE %:customerPhone% " +
            "AND customerOrder.createdDate >= :startDate " +
            "AND customerOrder.createdDate <= :endDate " +
            "AND customerOrder.orderCount >= :orderCountMin " +
            "AND customerOrder.orderCount <= :orderCountMax " +
            "ORDER BY customerOrder.createdDate ASC")
    List<CustomerCareCustomerOrderDTO> findListCustomerOrder(@Param("customerPhone") String customerPhone,
                                                             @Param("startDate") Instant startDate,
                                                             @Param("endDate") Instant endDate,
                                                             @Param("orderCountMin") int orderCountMin,
                                                             @Param("orderCountMax") int orderCountMax);

    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO(customerOrder, customer, store) " +
            "FROM CustomerOrderEntity customerOrder " +
            "JOIN vn.com.buaansach.entity.customer.CustomerEntity customer " +
            "ON customerOrder.customerPhone = customer.customerPhone " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON customerOrder.storeGuid = store.guid " +
            "WHERE customerOrder.customerPhone LIKE %:customerPhone% " +
            "AND customerOrder.createdDate >= :startDate " +
            "AND customerOrder.createdDate <= :endDate " +
            "AND customerOrder.orderCount >= :orderCountMin " +
            "ORDER BY customerOrder.createdDate ASC")
    List<CustomerCareCustomerOrderDTO> findListCustomerOrder(@Param("customerPhone") String customerPhone,
                                                             @Param("startDate") Instant startDate,
                                                             @Param("endDate") Instant endDate,
                                                             @Param("orderCountMin") int orderCountMin);

    Optional<CustomerOrderEntity> findOneByGuid(UUID guid);
}
