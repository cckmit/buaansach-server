package vn.com.buaansach.web.rest.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.dto.EmployeeStoreUserDTO;
import vn.com.buaansach.service.employee.EmployeeStoreUserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/store-user")
public class EmployeeStoreUserResource {
    private final String ENTITY_NAME = "employee-store-user";
    private final Logger log = LoggerFactory.getLogger(EmployeeStoreUserResource.class);
    private final EmployeeStoreUserService employeeStoreUserService;

    public EmployeeStoreUserResource(EmployeeStoreUserService employeeStoreUserService) {
        this.employeeStoreUserService = employeeStoreUserService;
    }

    @GetMapping("/list-by-user")
    public ResponseEntity<List<EmployeeStoreUserDTO>> getListStoreUserByUser() {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user {} to list {}", currentUser, ENTITY_NAME);
        return ResponseEntity.ok(employeeStoreUserService.getListStoreUserByUser(currentUser));
    }
}
