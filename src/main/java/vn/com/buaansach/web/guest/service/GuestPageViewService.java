package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.statistic.PageViewEntity;
import vn.com.buaansach.web.guest.repository.statistic.GuestPageViewRepository;

@Service
@RequiredArgsConstructor
public class GuestPageViewService {
    private final GuestPageViewRepository guestPageViewRepository;

    public void addPageView(PageViewEntity entity) {
        guestPageViewRepository.save(entity);
    }
}
