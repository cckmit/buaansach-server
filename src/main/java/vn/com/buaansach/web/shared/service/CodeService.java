package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.SequenceEntity;
import vn.com.buaansach.entity.enumeration.FixedSequence;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.web.shared.repository.common.SequenceRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CodeService {
    public static String STORE_CODE_PREFIX = "BAS";
    public static int STORE_CODE_LENGTH = 3;

    public static String PRODUCT_CODE_PREFIX = "SP";
    public static int PRODUCT_CODE_LENGTH = 3;

    public static int INTERNAL_USER_CODE_LENGTH = 4;
    public static int CUSTOMER_USER_CODE_LENGTH = 8;

    public static int ORDER_CODE_LENGTH = 7; // Prefix is 3 digits of store code;

    private final SequenceRepository sequenceRepository;

    @Transactional
    public int getNextSequenceById(String sequenceId) {
        SequenceEntity sequenceEntity = sequenceRepository.findOneBySequenceId(sequenceId)
                .orElseThrow();
        int nextNumber = sequenceEntity.getSequenceNumber() + 1;
        sequenceEntity.setSequenceNumber(nextNumber);
        sequenceRepository.save(sequenceEntity);
        return nextNumber;
    }

    @Transactional
    public String generateCodeForOrder(StoreEntity storeEntity) {
        int nextNumber = getNextSequenceById(storeEntity.getStoreCode());
        return storeEntity.getStoreCode().replace(STORE_CODE_PREFIX, "") + StringUtils.leftPad("" + nextNumber, ORDER_CODE_LENGTH, "0");
    }

    @Transactional
    public String generateCodeForCustomerUser() {
        int nextNumber = getNextSequenceById(FixedSequence.CUSTOMER_USER.name());
        return StringUtils.leftPad("" + nextNumber, CUSTOMER_USER_CODE_LENGTH, "0");
    }

    @Transactional
    public String generateCodeForStore() {
        int nextNumber = getNextSequenceById(FixedSequence.STORE_NUMBER.name());
        return STORE_CODE_PREFIX + StringUtils.leftPad("" + nextNumber, STORE_CODE_LENGTH, "0");
    }

    @Transactional
    public String generateCodeForProduct() {
        int nextNumber = getNextSequenceById(FixedSequence.PRODUCT_NUMBER.name());
        return PRODUCT_CODE_PREFIX + StringUtils.leftPad("" + nextNumber, PRODUCT_CODE_LENGTH, "0");
    }

    @Transactional
    public String generateCodeForInternalUser() {
        int nextNumber = getNextSequenceById(FixedSequence.INTERNAL_USER.name());
        return StringUtils.leftPad("" + nextNumber, INTERNAL_USER_CODE_LENGTH, "0");
    }
}
