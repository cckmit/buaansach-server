package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.web.shared.service.dto.read.StoreUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreUserRepository extends JpaRepository<StoreUserEntity, Long> {
    Optional<StoreUserEntity> findOneByUserGuidAndStoreGuid(UUID userGuid, UUID storeGuid);

    Optional<StoreUserEntity> findOneByGuid(UUID guid);

    void deleteByStoreGuid(UUID storeGuid);

    /* Use for all user */
    @Query("SELECT new vn.com.buaansach.web.shared.service.dto.read.StoreUserDTO(storeUser, store) " +
            "FROM StoreUserEntity storeUser " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON storeUser.storeGuid = store.guid " +
            "WHERE storeUser.userGuid = :userGuid " +
            "AND store.storeActivated = :storeActivated " +
            "AND storeUser.storeUserStatus = :storeUserStatus " +
            "AND storeUser.storeUserActivated = TRUE")
    List<StoreUserDTO> findListStoreUserDTOByUser(@Param("userGuid") UUID userGuid,
                                                  @Param("storeActivated") boolean storeActivated,
                                                  @Param("storeUserStatus") StoreUserStatus storeUserStatus);
}
