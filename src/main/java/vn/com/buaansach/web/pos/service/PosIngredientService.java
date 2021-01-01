package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.IngredientEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.common.AdminIngredientRepository;
import vn.com.buaansach.web.admin.repository.common.AdminProductIngredientRepository;
import vn.com.buaansach.web.pos.repository.common.PosIngredientRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PosIngredientService {
    private final PosIngredientRepository adminIngredientRepository;

    public List<IngredientEntity> getIngredients(){
        return adminIngredientRepository.findAllByOrderByIngredientPosition();
    }

}
