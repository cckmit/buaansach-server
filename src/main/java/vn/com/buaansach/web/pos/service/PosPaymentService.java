package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.enumeration.PaymentMethod;
import vn.com.buaansach.entity.enumeration.PaymentStatus;
import vn.com.buaansach.web.pos.repository.PosPaymentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosPaymentService {
    private final PosPaymentRepository posPaymentRepository;

    public PaymentEntity makeCashPayment(UUID orderGuid, String note, long totalAmount) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setGuid(UUID.randomUUID());
        paymentEntity.setOrderGuid(orderGuid);
        paymentEntity.setPaymentNote(note);
        paymentEntity.setTotalAmount(totalAmount);
        paymentEntity.setPaymentMethod(PaymentMethod.CASH);
        paymentEntity.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        return posPaymentRepository.save(paymentEntity);
    }

    public void proceedPayment() {

    }

    public void cancelPayment() {

    }
}
