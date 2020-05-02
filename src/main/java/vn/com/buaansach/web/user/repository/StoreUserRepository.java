package vn.com.buaansach.web.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.web.user.service.dto.StoreUserDTO;

import java.util.List;

@Repository
public interface StoreUserRepository extends JpaRepository<StoreUserEntity, Long> {

    /* Use for all user */
    @Query("SELECT new vn.com.buaansach.web.user.service.dto.StoreUserDTO(storeUser, store, user) " +
            "FROM vn.com.buaansach.entity.store.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON storeUser.storeGuid = store.guid " +
            "LEFT JOIN vn.com.buaansach.entity.user.UserEntity user " +
            "ON storeUser.userLogin = user.login " +
            "WHERE storeUser.userLogin = :userLogin " +
            "AND store.storeActivated = TRUE " +
            "AND storeUser.storeUserStatus = 'WORKING'")
    List<StoreUserDTO> findListStoreUserByUser(@Param("userLogin") String userLogin);
}
