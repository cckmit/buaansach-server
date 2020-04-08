package vn.com.buaansach.service.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.AccessDeniedException;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.LoginAlreadyUsedException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.service.dto.StoreUserDTO;
import vn.com.buaansach.service.dto.manipulation.AddStoreUserDTO;
import vn.com.buaansach.service.dto.manipulation.CreateOrUpdateStoreUserDTO;
import vn.com.buaansach.entity.AuthorityEntity;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.entity.enumeration.Language;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.repository.StoreUserRepository;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.StoreUserSecurityService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AdminStoreUserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreUserRepository storeUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreUserSecurityService storeUserSecurityService;

    public AdminStoreUserService(UserRepository userRepository, StoreRepository storeRepository, StoreUserRepository storeUserRepository, PasswordEncoder passwordEncoder, StoreUserSecurityService storeUserSecurityService) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.storeUserRepository = storeUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeUserSecurityService = storeUserSecurityService;
    }

    public StoreUserDTO addStoreUser(AddStoreUserDTO request) {
        /* check user existence */
        String loginOrEmail = request.getUserLoginOrEmail().toLowerCase();
        UserEntity userEntity = userRepository.findOneByLoginOrEmail(loginOrEmail, loginOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("user", "loginOrEmail", loginOrEmail));

        /* check store existence */
        storeRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("store", "guid", request.getStoreGuid()));

        StoreUserEntity storeUserEntity = new StoreUserEntity();
        storeUserEntity.setGuid(UUID.randomUUID());
        storeUserEntity.setStoreGuid(request.getStoreGuid());
        storeUserEntity.setUserLogin(userEntity.getLogin());
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());
        return new StoreUserDTO(storeUserRepository.save(storeUserEntity), userEntity);
    }

    @Transactional
    public StoreUserDTO createStoreUser(CreateOrUpdateStoreUserDTO request) {
        /* check store existence */
        storeRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("store", "guid", request.getStoreGuid()));

        UserEntity userEntity = createUser(request);

        StoreUserEntity storeUserEntity = new StoreUserEntity();
        storeUserEntity.setGuid(UUID.randomUUID());
        storeUserEntity.setStoreGuid(request.getStoreGuid());
        storeUserEntity.setUserLogin(userEntity.getLogin());
        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());

        return new StoreUserDTO(storeUserRepository.save(storeUserEntity), userEntity);
    }

    @Transactional
    public StoreUserDTO updateStoreUser(CreateOrUpdateStoreUserDTO request) {
        StoreUserEntity storeUserEntity = storeUserRepository.findOneByGuid(request.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("store", "guid", request.getGuid()));

        storeUserEntity.setStoreUserRole(request.getStoreUserRole());
        storeUserEntity.setStoreUserStatus(request.getStoreUserStatus());
        StoreUserEntity updatedStoreUserEntity = storeUserRepository.save(storeUserEntity);

        /* We dont allow to modify user login
        => do not use userLogin from request, cause it might be modified */
        UserEntity updatedUserEntity = updateUser(storeUserEntity.getUserLogin(), request);

        return new StoreUserDTO(updatedStoreUserEntity, updatedUserEntity);
    }

    public List<StoreUserDTO> getListStoreUserByStoreGuid(String storeGuid) {
        return storeUserRepository.findByStoreGuid(UUID.fromString(storeGuid));
    }

    public void toggleAccount(String storeUserGuid) {
        StoreUserEntity storeUserEntity = storeUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new ResourceNotFoundException("storeUser", "guid", storeUserGuid));

        if (SecurityUtils.getCurrentUserLogin().equals(storeUserEntity.getUserLogin()))
            throw new BadRequestException("Bạn không thể vô hiệu hóa tài khoản của bạn");

        UserEntity userEntity = userRepository.findOneByLogin(storeUserEntity.getUserLogin())
                .orElseThrow(() -> new ResourceNotFoundException("user", "login", storeUserEntity.getUserLogin()));
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)
                && userEntity.getAuthorities().contains(new AuthorityEntity(AuthoritiesConstants.ADMIN))) {
            throw new AccessDeniedException("Bạn không có quyền vô hiệu hóa tài khoản của quản trị viên");
        }
        if (userEntity.isDisabledByAdmin() && !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            throw new AccessDeniedException("Tài khoản đã bị vô hiệu hóa bởi quản trị viên.");
        }
        userEntity.setActivated(!userEntity.isActivated());
        userRepository.save(userEntity);
    }

    private UserEntity createUser(CreateOrUpdateStoreUserDTO request) {
        UserEntity userEntity = new UserEntity();

        if (userRepository.findOneByLogin(request.getUserLogin()).isPresent()) {
            throw new LoginAlreadyUsedException();
        }

        userEntity.setLogin(request.getUserLogin());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setActivated(true);
        userEntity.setLangKey(Language.VIETNAMESE.getValue());
        Set<AuthorityEntity> authorities = new HashSet<>();
        authorities.add(new AuthorityEntity(AuthoritiesConstants.USER));
        userEntity.setAuthorities(authorities);
        return userRepository.save(userEntity);
    }

    private UserEntity updateUser(String userLogin, CreateOrUpdateStoreUserDTO request) {
        UserEntity updateUser = userRepository.findOneByLogin(userLogin)
                .orElseThrow(() -> new ResourceNotFoundException("user", "login", userLogin));
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
        return hasChanges ? userRepository.save(updateUser) : updateUser;
    }

    public void deleteStoreUser(String storeUserGuid) {
        StoreUserEntity storeUserEntity = storeUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new ResourceNotFoundException("storeUser", "guid", storeUserGuid));
        storeUserRepository.delete(storeUserEntity);
    }
}
