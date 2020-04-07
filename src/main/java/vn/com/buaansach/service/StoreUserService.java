package vn.com.buaansach.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AuthorityEntity;
import vn.com.buaansach.entity.StoreUserEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.entity.enumeration.Language;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.LoginAlreadyUsedException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.repository.StoreUserRepository;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.dto.StoreUserDTO;
import vn.com.buaansach.service.request.AddStoreUserRequest;
import vn.com.buaansach.service.request.CreateOrUpdateStoreUserRequest;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class StoreUserService {
    private final StoreUserRepository storeUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreUserSecurityService storeUserSecurityService;

    public StoreUserService(StoreUserRepository storeUserRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, StoreRepository storeRepository, StoreUserSecurityService storeUserSecurityService) {
        this.storeUserRepository = storeUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.storeUserSecurityService = storeUserSecurityService;
    }

    @Transactional
    public StoreUserDTO createStoreUser(CreateOrUpdateStoreUserRequest request) {
        /* check if user is not admin nor store owner nor store manager */
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(request.getStoreGuid());

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

    public StoreUserDTO addStoreUser(AddStoreUserRequest request) {
        /* check user role */
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(request.getStoreGuid());

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
    public StoreUserDTO updateStoreUser(CreateOrUpdateStoreUserRequest request) {
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(request.getStoreGuid());

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
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(UUID.fromString(storeGuid));
        return storeUserRepository.findByStoreGuid(UUID.fromString(storeGuid));
    }

    public void toggleAccount(String storeUserGuid) {

        StoreUserEntity storeUserEntity = storeUserRepository.findOneByGuid(UUID.fromString(storeUserGuid))
                .orElseThrow(() -> new ResourceNotFoundException("storeUser", "guid", storeUserGuid));

        if (SecurityUtils.getCurrentUserLogin().equals(storeUserEntity.getUserLogin()))
            throw new BadRequestException("You cannot deactivate yourself");

        storeUserSecurityService.blockAccessIfNotOwnerOrManager(storeUserEntity.getStoreGuid());

        UserEntity userEntity = userRepository.findOneByLogin(storeUserEntity.getUserLogin())
                .orElseThrow(() -> new ResourceNotFoundException("user", "login", storeUserEntity.getUserLogin()));

        userEntity.setActivated(!userEntity.isActivated());
        userRepository.save(userEntity);
    }

    private UserEntity createUser(CreateOrUpdateStoreUserRequest request) {
        UserEntity userEntity = new UserEntity();

        if (userRepository.findOneByLogin(request.getUserLogin()).isPresent()) {
            throw new LoginAlreadyUsedException();
        }

        userEntity.setLogin(request.getUserLogin());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setActivated(true);
        userEntity.setLangKey(Language.vi.name());
        Set<AuthorityEntity> authorities = new HashSet<>();
        authorities.add(new AuthorityEntity(AuthoritiesConstants.USER));
        userEntity.setAuthorities(authorities);
        return userRepository.save(userEntity);
    }

    private UserEntity updateUser(String userLogin, CreateOrUpdateStoreUserRequest request) {
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
}
