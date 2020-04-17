package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminStoreUserRepository extends JpaRepository<StoreUserEntity, Long> {
    Optional<StoreUserEntity> findOneByUserLoginAndStoreGuid(String userLogin, UUID storeGuid);

    Optional<StoreUserEntity> findOneByGuid(UUID guid);

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO(storeUser, user) " +
            "FROM vn.com.buaansach.entity.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.UserEntity user " +
            "ON storeUser.userLogin = user.login " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<AdminStoreUserDTO> findByStoreGuid(@Param("storeGuid") UUID storeGuid);

    void deleteByStoreGuid(UUID storeGuid);
}
