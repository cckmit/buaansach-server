package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.CategoryEntity;
import vn.com.buaansach.web.guest.repository.GuestCategoryRepository;

import java.util.List;

@Service
public class GuestCategoryService {
    private final GuestCategoryRepository guestCategoryRepository;

    public GuestCategoryService(GuestCategoryRepository guestCategoryRepository) {
        this.guestCategoryRepository = guestCategoryRepository;
    }

    public List<CategoryEntity> getListGuestCategoryDTO() {
        return guestCategoryRepository.findAll();
    }
}
