package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.web.employee.service.dto.EmployeeStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.AdminStoreUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreUserRepository extends JpaRepository<StoreUserEntity, Long> {
    Optional<StoreUserEntity> findOneByUserLoginAndStoreGuid(String userLogin, UUID storeGuid);

    Optional<StoreUserEntity> findOneByGuid(UUID guid);

    @Query("SELECT new vn.com.buaansach.web.common.service.dto.StoreUserDTO(storeUser, user) " +
            "FROM vn.com.buaansach.entity.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.UserEntity user " +
            "ON storeUser.userLogin = user.login " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<AdminStoreUserDTO> findByStoreGuid(@Param("storeGuid") UUID storeGuid);

    @Query("SELECT new vn.com.buaansach.web.employee.service.dto.EmployeeStoreUserDTO(storeUser, store, user) " +
            "FROM vn.com.buaansach.entity.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.StoreEntity store " +
            "ON storeUser.storeGuid = store.guid " +
            "LEFT JOIN vn.com.buaansach.entity.UserEntity user " +
            "ON storeUser.userLogin = user.login " +
            "WHERE storeUser.userLogin = :userLogin")
    List<EmployeeStoreUserDTO> findListStoreUserByUser(@Param("userLogin") String userLogin);

    void deleteByStoreGuid(UUID guid);
}
