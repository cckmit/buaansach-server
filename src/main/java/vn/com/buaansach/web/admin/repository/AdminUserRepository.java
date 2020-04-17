package vn.com.buaansach.web.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.UserEntity;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findOneByActivationKey(String activationKey);

    Optional<UserEntity> findOneByResetKey(String resetKey);

    Optional<UserEntity> findOneByEmailIgnoreCase(String email);

    Optional<UserEntity> findOneByLogin(String login);

    Optional<UserEntity> findOneByLoginOrEmail(String login, String email);

    Optional<UserEntity> findOneByPhone(String phone);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<UserEntity> findAllByLoginNot(Pageable pageable, String login);

    @Query("SELECT u FROM UserEntity u WHERE u.login LIKE %:search% OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%")
    Page<UserEntity> findPageUserWithKeyword(Pageable pageable, @Param("search") String search);
}
