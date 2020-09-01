package vn.com.buaansach.web.admin.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.core.repository.user.UserRepository;
import vn.com.buaansach.entity.user.UserEntity;

public interface AdminUserRepository extends UserRepository {
    @Query("SELECT u FROM UserEntity u WHERE u.userLogin LIKE %:search% OR u.userProfile.fullName LIKE %:search% OR u.userProfile.userCode LIKE %:search%")
    Page<UserEntity> findPageUserWithKeyword(Pageable pageable, @Param("search") String search);
}
