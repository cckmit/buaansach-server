package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.core.repository.store.StoreUserRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreUserRepository extends StoreUserRepository {


    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO(storeUser, user) " +
            "FROM vn.com.buaansach.entity.store.StoreUserEntity storeUser " +
            "LEFT JOIN vn.com.buaansach.entity.user.UserProfileEntity user " +
            "ON storeUser.userLogin = user.login " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<AdminStoreUserDTO> findDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);
}
