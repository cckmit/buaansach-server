package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosStoreUserRepository extends JpaRepository<StoreUserEntity, Long> {
    Optional<StoreUserEntity> findOneByUserLoginAndStoreGuid(String userLogin, UUID storeGuid);

    Optional<StoreUserEntity> findOneByGuid(UUID guid);

    void deleteByStoreGuid(UUID storeGuid);

    List<StoreUserEntity> findByStoreGuid(UUID storeGuid);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO(storeUser, user) " +
            "FROM StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.user.UserEntity user " +
            "ON storeUser.userLogin = user.userLogin " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<PosStoreUserDTO> findListDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);

    Optional<StoreUserEntity> findByStoreGuidAndUserLogin(UUID storeGuid, String userLogin);

}
