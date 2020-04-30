package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.enumeration.PaymentMethod;
import vn.com.buaansach.entity.enumeration.PaymentStatus;
import vn.com.buaansach.web.pos.repository.PosPaymentRepository;

import java.util.UUID;

@Service
public class PosPaymentService {
    private final PosPaymentRepository posPaymentRepository;

    public PosPaymentService(PosPaymentRepository posPaymentRepository) {
        this.posPaymentRepository = posPaymentRepository;
    }

    public PaymentEntity makeCashPayment(UUID orderGuid, long totalCharge) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setGuid(UUID.randomUUID());
        paymentEntity.setOrderGuid(orderGuid);
        paymentEntity.setTotalCharge(totalCharge);
        paymentEntity.setPaymentMethod(PaymentMethod.CASH);
        paymentEntity.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        return posPaymentRepository.save(paymentEntity);
    }

    public void proceedPayment() {

    }

    public void cancelPayment() {

    }
}
