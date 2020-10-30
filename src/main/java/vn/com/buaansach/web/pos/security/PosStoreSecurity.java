package vn.com.buaansach.web.pos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.ForbiddenException;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreUserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosStoreSecurity {
    private final PosStoreUserRepository posStoreUserRepository;
    private final PosStoreRepository posStoreRepository;

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

        StoreEntity storeEntity = posStoreRepository.findOneByGuid(storeGuid).orElse(null);
        if (storeEntity == null || !storeEntity.isStoreActivated()) return false;

        return posStoreUserRepository.findOneByUserLoginAndStoreGuid(currentUserLogin, storeGuid)
                .map(storeUserEntity -> roles.contains(storeUserEntity.getStoreUserRole())
                        /* if user is not working in this store => return false too */
                        && storeUserEntity.isStoreUserActivated()
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
            throw new ForbiddenException();
    }

    public void blockAccessIfNotOwner(UUID storeGuid) {
        if (!hasOwnerPermission(storeGuid))
            throw new ForbiddenException();
    }

    public void blockAccessIfNotInStore(UUID storeGuid) {
        if (!hasPermission(storeGuid))
            throw new ForbiddenException(ErrorCode.USER_NOT_IN_STORE);
    }

    public void blockAccessIfNotInStoreAndArea(UUID storeGuid, UUID areaGuid) {
        if (!hasPermission(storeGuid))
            throw new ForbiddenException(ErrorCode.USER_NOT_IN_STORE);
        StoreUserEntity storeUserEntity = posStoreUserRepository.findOneByUserLoginAndStoreGuid(SecurityUtils.getCurrentUserLogin(), storeGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));
        if (areaGuid != null && storeUserEntity.getStoreUserArea() == null || storeUserEntity.getStoreUserArea().isBlank())
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        if (areaGuid != null && !storeUserEntity.getStoreUserArea().contains(areaGuid.toString()))
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
    }
}
