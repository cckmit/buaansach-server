package vn.com.buaansach.web.admin.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.web.admin.service.dto.read.AdminUserDTO;
import vn.com.buaansach.web.shared.repository.user.UserRepository;
import vn.com.buaansach.entity.user.UserEntity;

public interface AdminUserRepository extends UserRepository {

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminUserDTO(user, profile) " +
            "FROM UserEntity user " +
            "JOIN vn.com.buaansach.entity.user.UserProfileEntity profile " +
            "ON user.guid = profile.userGuid " +
            "WHERE user.userLogin LIKE %:search% " +
            "OR user.userEmail LIKE %:search% " +
            "OR user.userPhone LIKE %:search% " +
            "OR profile.fullName LIKE %:search% " +
            "OR profile.userCode LIKE %:search%")
    Page<AdminUserDTO> findPageUserWithKeyword(Pageable pageable, @Param("search") String search);
}
