package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.ForbiddenException;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreUserRepository;
import vn.com.buaansach.web.admin.repository.user.AdminUserProfileRepository;
import vn.com.buaansach.web.admin.repository.user.AdminUserRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.read.AdminUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminAddStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateOrUpdateStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateUserDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AdminStoreUserService {
    private final AdminUserRepository adminUserRepository;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminStoreUserRepository adminStoreUserRepository;
    private final AdminUserProfileRepository adminUserProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserService adminUserService;

    public AdminStoreUserDTO addStoreUser(AdminAddStoreUserDTO request) {
        /* check user existence */
        String principal = request.getUserPrincipal().toLowerCase();
        UserEntity userEntity = adminUserRepository.findOneByUserLoginIgnoreCaseOrUserEmailIgnoreCaseOrUserPhone(principal, principal, principal)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        UserProfileEntity userProfileEntity = adminUserProfileRepository.findOneByUserGuid(userEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));

        /* check store existence */
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        if (adminStoreUserRepository.findOneByUserLoginAndStoreGuid(userEntity.getUserLogin(), request.getStoreGuid()).isPresent())
            throw new BadRequestException(ErrorCode.STORE_USER_EXIST);

        StoreUserEntity storeUserEntity = new StoreUserEntity();
        storeUserEntity.setGuid(UUID.randomUUID());
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());
        storeUserEntity.setStoreUserActivated(true);
        storeUserEntity.setStoreGuid(request.getStoreGuid());
        storeUserEntity.setUserLogin(userEntity.getUserLogin());

        return new AdminStoreUserDTO(adminStoreUserRepository.save(storeUserEntity), userEntity, userProfileEntity);
    }

    @Transactional
    public AdminStoreUserDTO createStoreUser(AdminCreateOrUpdateStoreUserDTO request) {
        /* check store existence */
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        AdminCreateUserDTO createUserDTO = new AdminCreateUserDTO(request);

        AdminUserDTO dto = adminUserService.createUser(createUserDTO);

        StoreUserEntity storeUserEntity = new StoreUserEntity();
        storeUserEntity.setGuid(UUID.randomUUID());
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());
        storeUserEntity.setStoreUserActivated(true);
        storeUserEntity.setStoreGuid(request.getStoreGuid());
        storeUserEntity.setUserLogin(request.getUserLogin());

        return new AdminStoreUserDTO(adminStoreUserRepository.save(storeUserEntity), dto);
    }

    @Transactional
    public AdminStoreUserDTO updateStoreUser(AdminCreateOrUpdateStoreUserDTO request) {
        StoreUserEntity storeUserEntity = adminStoreUserRepository.findOneByGuid(request.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());

        /* do not use userLogin from request, because it might be modified */
        UserEntity updateUser = adminUserRepository.findOneByUserLoginIgnoreCase(storeUserEntity.getUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        if (request.getUserPassword() != null && !request.getUserPassword().isEmpty()) {
            updateUser.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
            updateUser = adminUserRepository.save(updateUser);
        }

        UserProfileEntity profileEntity = adminUserProfileRepository.findOneByUserGuid(updateUser.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));
        if (!profileEntity.getFullName().equals(request.getFullName())) {
            profileEntity.setFullName(request.getFullName());
            profileEntity = adminUserProfileRepository.save(profileEntity);
        }

        return new AdminStoreUserDTO(adminStoreUserRepository.save(storeUserEntity), updateUser, profileEntity);
    }

    public List<AdminStoreUserDTO> getListStoreUserByStoreGuid(String storeGuid) {
        return adminStoreUserRepository.findDTOByStoreGuid(UUID.fromString(storeGuid));
    }

    public void toggleStoreUserActivation(String storeUserGuid) {
        StoreUserEntity storeUserEntity = adminStoreUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));

        if (SecurityUtils.getCurrentUserLogin().equals(storeUserEntity.getUserLogin()))
            throw new ForbiddenException(ErrorCode.INVALID_OPERATION);
        storeUserEntity.setStoreUserActivated(!storeUserEntity.isStoreUserActivated());
        adminStoreUserRepository.save(storeUserEntity);
    }

    /* remove user from store, but account will remain in system */
    public void deleteStoreUser(String storeUserGuid) {
        StoreUserEntity storeUserEntity = adminStoreUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));
        adminStoreUserRepository.delete(storeUserEntity);
    }

}
