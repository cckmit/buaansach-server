package vn.com.buaansach.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.web.common.service.dto.read.StoreUserDTO;

import java.util.List;

@Repository
public interface CommonStoreUserRepository extends JpaRepository<StoreUserEntity, Long> {

    /* Use for all user */
    @Query("SELECT new vn.com.buaansach.web.common.service.dto.StoreUserDTO(storeUser, store) " +
            "FROM vn.com.buaansach.entity.store.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON storeUser.storeGuid = store.guid " +
            "WHERE storeUser.userLogin = :userLogin " +
            "AND store.storeActivated = :storeActivated " +
            "AND storeUser.storeUserStatus = :storeUserStatus")
    List<StoreUserDTO> findListStoreUserByUser(@Param("userLogin") String userLogin,
                                               @Param("storeActivated") boolean storeActivated,
                                               @Param("storeUserStatus") StoreUserStatus storeUserStatus);
}
