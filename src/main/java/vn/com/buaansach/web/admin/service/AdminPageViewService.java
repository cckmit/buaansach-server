package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.statistic.PageViewEntity;
import vn.com.buaansach.web.admin.repository.statistic.AdminPageViewRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminPageViewDTO;
import vn.com.buaansach.web.guest.repository.statistic.GuestPageViewRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPageViewService {
    private final AdminPageViewRepository adminPageViewRepository;

    public AdminPageViewDTO getPageViewStatistic(AdminPageViewDTO payload){
        if (payload.getEndDate() == null) payload.setEndDate(Instant.now());
        payload.setTotalPageView(adminPageViewRepository.countTotalPageView(payload.getStartDate(), payload.getEndDate()));
        payload.setUniqueVisitor(adminPageViewRepository.countUniqueVisitor(payload.getStartDate(), payload.getEndDate()));
        return payload;
    }
}
