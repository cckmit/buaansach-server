package vn.com.buaansach.web.admin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AuthorityEntity;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.exception.AccessDeniedException;
import vn.com.buaansach.exception.LoginAlreadyUsedException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreUserRepository;
import vn.com.buaansach.web.admin.repository.AdminUserRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminAddStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateOrUpdateStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AdminStoreUserService {
    private final AdminUserRepository adminUserRepository;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminStoreUserRepository adminStoreUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminStoreUserService(AdminUserRepository adminUserRepository, AdminStoreRepository adminStoreRepository, AdminStoreUserRepository adminStoreUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.adminStoreRepository = adminStoreRepository;
        this.adminStoreUserRepository = adminStoreUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminStoreUserDTO addStoreUser(AdminAddStoreUserDTO request) {
        /* check user existence */
        String loginOrEmail = request.getUserLoginOrEmail().toLowerCase();
        UserEntity userEntity = adminUserRepository.findOneByLoginOrEmail(loginOrEmail, loginOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username or email:" + loginOrEmail));

        /* check store existence */
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        StoreUserEntity storeUserEntity = new StoreUserEntity();
        storeUserEntity.setGuid(UUID.randomUUID());
        storeUserEntity.setStoreGuid(request.getStoreGuid());
        storeUserEntity.setUserLogin(userEntity.getLogin());
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());
        return new AdminStoreUserDTO(adminStoreUserRepository.save(storeUserEntity), userEntity);
    }

    @Transactional
    public AdminStoreUserDTO createStoreUser(AdminCreateOrUpdateStoreUserDTO request) {
        /* check store existence */
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        UserEntity userEntity = createUser(request);

        StoreUserEntity storeUserEntity = new StoreUserEntity();
        storeUserEntity.setGuid(UUID.randomUUID());
        storeUserEntity.setStoreGuid(request.getStoreGuid());
        storeUserEntity.setUserLogin(userEntity.getLogin());
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());

        return new AdminStoreUserDTO(adminStoreUserRepository.save(storeUserEntity), userEntity);
    }

    @Transactional
    public AdminStoreUserDTO updateStoreUser(AdminCreateOrUpdateStoreUserDTO request) {
        StoreUserEntity storeUserEntity = adminStoreUserRepository.findOneByGuid(request.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store User not found with guid: " + request.getGuid()));

        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());
        StoreUserEntity updatedStoreUserEntity = adminStoreUserRepository.save(storeUserEntity);

        /* disallow to modify user login
        => do not use userLogin from request, because it might be modified */
        UserEntity updatedUserEntity = updateUser(storeUserEntity.getUserLogin(), request);

        return new AdminStoreUserDTO(updatedStoreUserEntity, updatedUserEntity);
    }

    public List<AdminStoreUserDTO> getListStoreUserByStoreGuid(String storeGuid) {
        return adminStoreUserRepository.findByStoreGuid(UUID.fromString(storeGuid));
    }

    public void toggleAccountActivation(String storeUserGuid) {
        StoreUserEntity storeUserEntity = adminStoreUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store User not found with guid: " + storeUserGuid));

        if (SecurityUtils.getCurrentUserLogin().equals(storeUserEntity.getUserLogin()))
            throw new AccessDeniedException("You cannot deactivate your account");

        UserEntity userEntity = adminUserRepository.findOneByLogin(storeUserEntity.getUserLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + storeUserEntity.getUserLogin()));

        /* when user doesn't have role admin try to change activate status of admin account */
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)
                && userEntity.getAuthorities().contains(new AuthorityEntity(AuthoritiesConstants.ADMIN))) {
            throw new AccessDeniedException("Cannot deactivate administrator's account");
        }

        /* when user doesn't have role admin try to change activate status of account that was disabled by admin account */
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && userEntity.isDisabledByAdmin()) {
            throw new AccessDeniedException("Account has been deactivate by administrator.");
        }
        userEntity.setActivated(!userEntity.isActivated());
        adminUserRepository.save(userEntity);
    }

    private UserEntity createUser(AdminCreateOrUpdateStoreUserDTO request) {
        UserEntity userEntity = new UserEntity();

        if (adminUserRepository.findOneByLogin(request.getUserLogin()).isPresent()) {
            throw new LoginAlreadyUsedException();
        }
        userEntity.setGuid(UUID.randomUUID());
        userEntity.setLogin(request.getUserLogin());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setActivated(true);
        userEntity.setLangKey(Constants.DEFAULT_LANGUAGE);
        Set<AuthorityEntity> authorities = new HashSet<>();
        authorities.add(new AuthorityEntity(AuthoritiesConstants.USER));
        userEntity.setAuthorities(authorities);
        return adminUserRepository.save(userEntity);
    }

    private UserEntity updateUser(String userLogin, AdminCreateOrUpdateStoreUserDTO request) {
        UserEntity updateUser = adminUserRepository.findOneByLogin(userLogin)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + userLogin));
        boolean hasChanges = false;
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            updateUser.setPassword(passwordEncoder.encode(request.getPassword()));
            hasChanges = true;
        }
        if (!updateUser.getFirstName().equals(request.getFirstName()) ||
                !updateUser.getLastName().equals(request.getLastName())) {
            updateUser.setFirstName(request.getFirstName());
            updateUser.setLastName(request.getLastName());
            hasChanges = true;
        }
        return hasChanges ? adminUserRepository.save(updateUser) : updateUser;
    }

    /* remove user from store, but account will remain in system */
    public void deleteStoreUser(String storeUserGuid) {
        StoreUserEntity storeUserEntity = adminStoreUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store User not found with guid: " + storeUserGuid));
        adminStoreUserRepository.delete(storeUserEntity);
    }

}
