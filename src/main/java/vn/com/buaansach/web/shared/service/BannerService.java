package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.brand.BannerEntity;
import vn.com.buaansach.web.shared.repository.brand.BannerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    public List<BannerEntity> getListBanner(){
        return bannerRepository.findAll();
    }
}
