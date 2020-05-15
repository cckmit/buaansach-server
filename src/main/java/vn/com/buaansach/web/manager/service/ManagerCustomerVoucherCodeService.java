package vn.com.buaansach.web.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.manager.repository.ManagerCustomerRepository;
import vn.com.buaansach.web.manager.repository.ManagerVoucherRepository;
import vn.com.buaansach.web.manager.service.dto.ManagerCustomerVoucherCodeDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerCustomerVoucherCodeService {
    private final ManagerCustomerRepository managerCustomerRepository;
    private final ManagerVoucherRepository managerVoucherRepository;

    public Page<ManagerCustomerVoucherCodeDTO> getPageCustomer(PageRequest request, String search) {
        VoucherEntity voucherEntity = managerVoucherRepository.findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID).orElseThrow();
        return managerCustomerRepository.findPageManagerCustomerCodeDTO(voucherEntity.getGuid(), search, request);
    }

    public List<ManagerCustomerVoucherCodeDTO> getListUnsentVoucher() {
        VoucherEntity voucherEntity = managerVoucherRepository.findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID).orElseThrow();
        return managerCustomerRepository.findUnsentVoucher(voucherEntity.getGuid(), VoucherCodeSentStatus.UNSET);
    }
}
