package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.store.StoreUserRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreUserRepository extends StoreUserRepository {
    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO(storeUser, user, profile) " +
            "FROM vn.com.buaansach.entity.store.StoreUserEntity storeUser " +
            "JOIN vn.com.buaansach.entity.user.UserEntity user " +
            "ON storeUser.userLogin = user.userLogin " +
            "JOIN vn.com.buaansach.entity.user.UserProfileEntity profile " +
            "ON user.guid = profile.userGuid " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<AdminStoreUserDTO> findDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);
}
