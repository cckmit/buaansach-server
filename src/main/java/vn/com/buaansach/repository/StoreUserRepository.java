package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.model.entity.StoreUserEntity;
import vn.com.buaansach.model.dto.StoreUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreUserRepository extends JpaRepository<StoreUserEntity, Long> {
    Optional<StoreUserEntity> findOneByUserLoginAndStoreGuid(String userLogin, UUID storeGuid);

    Optional<StoreUserEntity> findOneByGuid(UUID guid);

    @Query("SELECT new vn.com.buaansach.service.dto.StoreUserDTO(storeUser, user) " +
            "FROM vn.com.buaansach.entity.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.UserEntity user " +
            "ON storeUser.userLogin = user.login " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<StoreUserDTO> findByStoreGuid(@Param("storeGuid") UUID storeGuid);
}
