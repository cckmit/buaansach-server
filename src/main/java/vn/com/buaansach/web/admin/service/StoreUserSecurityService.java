package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.AccessDeniedException;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.admin.repository.AdminStoreUserRepository;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class StoreUserSecurityService {
    private final AdminStoreUserRepository adminStoreUserRepository;

    public StoreUserSecurityService(AdminStoreUserRepository adminStoreUserRepository) {
        this.adminStoreUserRepository = adminStoreUserRepository;
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
        /* if admin, just return true */
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) return true;

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
            throw new AccessDeniedException();
    }

}
