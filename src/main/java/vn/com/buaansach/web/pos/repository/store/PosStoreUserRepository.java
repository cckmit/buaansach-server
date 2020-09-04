package vn.com.buaansach.web.pos.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.shared.repository.store.StoreUserRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosStoreUserRepository extends StoreUserRepository {

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO(storeUser, user) " +
            "FROM StoreUserEntity storeUser " +
            "JOIN vn.com.buaansach.entity.user.UserEntity user " +
            "ON storeUser.userLogin = user.userLogin " +
            "WHERE storeUser.storeGuid = :storeGuid")
    List<PosStoreUserDTO> findListDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);

}
