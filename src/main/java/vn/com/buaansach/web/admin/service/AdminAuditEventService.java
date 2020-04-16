package vn.com.buaansach.web.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.buaansach.config.audit.AuditEventConverter;
import vn.com.buaansach.web.admin.repository.AdminPersistenceAuditEventRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator {@code AuditEventRepository}.
 */
@Service
@Transactional
public class AdminAuditEventService {

    private final Logger log = LoggerFactory.getLogger(AdminAuditEventService.class);

    private final AdminPersistenceAuditEventRepository adminPersistenceAuditEventRepository;

    private final AuditEventConverter auditEventConverter;

    @Value("${audit-events.retention-period}")
    private int retentionPeriod;

    public AdminAuditEventService(
            AdminPersistenceAuditEventRepository adminPersistenceAuditEventRepository,
            AuditEventConverter auditEventConverter) {

        this.adminPersistenceAuditEventRepository = adminPersistenceAuditEventRepository;
        this.auditEventConverter = auditEventConverter;
    }

    /**
     * Old audit events should be automatically deleted after 30 days.
     * <p>
     * This is scheduled to get fired at 12:00 (am).
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void removeOldAuditEvents() {
        adminPersistenceAuditEventRepository
                .findByAuditEventDateBefore(Instant.now().minus(retentionPeriod, ChronoUnit.DAYS))
                .forEach(auditEvent -> {
                    log.debug("Deleting audit data {}", auditEvent);
                    adminPersistenceAuditEventRepository.delete(auditEvent);
                });
    }

    public Page<AuditEvent> findAll(Pageable pageable) {
        return adminPersistenceAuditEventRepository.findAll(pageable)
                .map(auditEventConverter::convertToAuditEvent);
    }

    public Page<AuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
        return adminPersistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable)
                .map(auditEventConverter::convertToAuditEvent);
    }

    public Optional<AuditEvent> find(Long id) {
        return adminPersistenceAuditEventRepository.findById(id)
                .map(auditEventConverter::convertToAuditEvent);
    }
}
