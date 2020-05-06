package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.AccessDeniedException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreUserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class StoreSecurityService {
    private final AdminStoreUserRepository adminStoreUserRepository;
    private final AdminStoreRepository adminStoreRepository;

    public StoreSecurityService(AdminStoreUserRepository adminStoreUserRepository, AdminStoreRepository adminStoreRepository) {
        this.adminStoreUserRepository = adminStoreUserRepository;
        this.adminStoreRepository = adminStoreRepository;
    }

    private Set<StoreUserRole> getOwnerRole() {
        Set<StoreUserRole> checkRoles = new HashSet<>();
        checkRoles.add(StoreUserRole.STORE_OWNER);
        return checkRoles;
    }

    private Set<StoreUserRole> getOwnerOrManagerRole() {
        Set<StoreUserRole> checkRoles = new HashSet<>();
        checkRoles.add(StoreUserRole.STORE_OWNER);
        checkRoles.add(StoreUserRole.STORE_MANAGER);
        return checkRoles;
    }

    private Set<StoreUserRole> getAllRole() {
        Set<StoreUserRole> checkRoles = new HashSet<>();
        checkRoles.add(StoreUserRole.STORE_OWNER);
        checkRoles.add(StoreUserRole.STORE_MANAGER);
        checkRoles.add(StoreUserRole.STORE_CASHIER);
        checkRoles.add(StoreUserRole.STORE_WAITER);
        return checkRoles;
    }

    private boolean checkAccess(Set<StoreUserRole> roles, UUID storeGuid) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        /*if allow Admin, uncomment it*/
//        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) return true;

        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(storeGuid).orElse(null);
        if (storeEntity == null) return false;
        if (!storeEntity.isStoreActivated()) return false;

        return adminStoreUserRepository.findOneByUserLoginAndStoreGuid(currentUserLogin, storeGuid)
                .map(storeUserEntity -> roles.contains(storeUserEntity.getStoreUserRole())
                        /* if user is not working in this store => return false too */
                        && storeUserEntity.getStoreUserStatus().equals(StoreUserStatus.WORKING)).orElse(false);
    }

    public boolean hasOwnerOrManagerPermission(UUID storeGuid) {
        return checkAccess(getOwnerOrManagerRole(), storeGuid);
    }

    public boolean hasOwnerPermission(UUID storeGuid) {
        return checkAccess(getOwnerRole(), storeGuid);
    }

    public boolean hasPermission(UUID storeGuid) {
        return checkAccess(getAllRole(), storeGuid);
    }

    public void blockAccessIfNotOwnerOrManager(UUID storeGuid) {
        if (!hasOwnerOrManagerPermission(storeGuid))
            throw new AccessDeniedException();
    }

    public void blockAccessIfNotOwner(UUID storeGuid) {
        if (!hasOwnerPermission(storeGuid))
            throw new AccessDeniedException();
    }

    public void blockAccessIfNotInStore(UUID storeGuid) {
        if (!hasPermission(storeGuid))
            throw new AccessDeniedException("You are not member of this store, contact admin or manager");
    }

    private boolean isClosedOrDeactivated(UUID storeGuid) {
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(storeGuid).orElse(null);
        return storeEntity == null || storeEntity.getStoreStatus().equals(StoreStatus.CLOSED) || !storeEntity.isStoreActivated();
    }

    public void blockAccessIfStoreIsNotOpenOrDeactivated(UUID storeGuid) {
        if (isClosedOrDeactivated(storeGuid)) throw new AccessDeniedException("Store has been closed");
    }

}
