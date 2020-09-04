package vn.com.buaansach.web.general.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.web.general.service.dto.read.StoreUserDTO;

import java.util.List;

@Repository
public interface GeneralStoreUserRepository extends JpaRepository<StoreUserEntity, Long> {

    /* Use for all user */
    @Query("SELECT new vn.com.buaansach.web.general.service.dto.read.StoreUserDTO(storeUser, store) " +
            "FROM StoreUserEntity storeUser " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON storeUser.storeGuid = store.guid " +
            "WHERE storeUser.userLogin = :userLogin " +
            "AND store.storeActivated = :storeActivated " +
            "AND storeUser.storeUserStatus = :storeUserStatus")
    List<StoreUserDTO> findListStoreUserByUser(@Param("userLogin") String userLogin,
                                               @Param("storeActivated") boolean storeActivated,
                                               @Param("storeUserStatus") StoreUserStatus storeUserStatus);
}
