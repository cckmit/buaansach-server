package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreWorkShiftUserDTO;
import vn.com.buaansach.web.shared.repository.store.StoreWorkShiftUserRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreWorkShiftUserRepository extends StoreWorkShiftUserRepository {

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreWorkShiftUserDTO(shiftUser, user, profile) " +
            "FROM StoreWorkShiftUserEntity shiftUser " +
            "JOIN vn.com.buaansach.entity.user.UserEntity user " +
            "ON shiftUser.userGuid = user.guid " +
            "JOIN vn.com.buaansach.entity.user.UserProfileEntity profile " +
            "ON user.guid = profile.userGuid " +
            "WHERE shiftUser.storeWorkShiftGuid = :storeWorkShiftGuid " +
            "ORDER BY shiftUser.id ASC")
    List<AdminStoreWorkShiftUserDTO> findListAdminStoreWorkShiftUserDTO(@Param("storeWorkShiftGuid") UUID storeWorkShiftGuid);
}
