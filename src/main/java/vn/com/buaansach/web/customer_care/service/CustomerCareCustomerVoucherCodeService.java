package vn.com.buaansach.web.customer_care.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.VoucherCodeClaimStatus;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.customer_care.repository.CustomerCareCustomerRepository;
import vn.com.buaansach.web.customer_care.repository.CustomerCareVoucherRepository;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareCustomerVoucherCodeDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerCareCustomerVoucherCodeService {
    private final CustomerCareCustomerRepository customerCareCustomerRepository;
    private final CustomerCareVoucherRepository customerCareVoucherRepository;

    public Page<CustomerCareCustomerVoucherCodeDTO> getPageCustomer(PageRequest request, String search) {
        VoucherEntity voucherEntity = customerCareVoucherRepository.findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID).orElseThrow();
        return customerCareCustomerRepository.findPageManagerCustomerCodeDTO(voucherEntity.getGuid(), search, request);
    }

    public List<CustomerCareCustomerVoucherCodeDTO> getListUnsentVoucher() {
        VoucherEntity voucherEntity = customerCareVoucherRepository.findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID).orElseThrow();
        return customerCareCustomerRepository.findUnsetVoucherCodeForCustomer(voucherEntity.getGuid(), VoucherCodeClaimStatus.UNSET);
    }
}
