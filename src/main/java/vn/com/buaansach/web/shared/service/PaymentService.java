package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.PaymentMethod;
import vn.com.buaansach.entity.enumeration.PaymentStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.web.shared.repository.order.PaymentRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PriceService priceService;

    public PaymentEntity makeCashPayment(String note, OrderEntity orderEntity) {
        int paymentAmount = priceService.calculatePayAmount(orderEntity);
        return saveCashPayment(note, paymentAmount, 1);
    }

    public PaymentEntity makeCashPayment(String note, List<OrderEntity> listOrder) {
        int paymentAmount = 0;
        for (OrderEntity orderEntity : listOrder) {
            paymentAmount += priceService.calculatePayAmount(orderEntity);
        }
        return saveCashPayment(note, paymentAmount, listOrder.size());
    }

    public PaymentEntity saveCashPayment(String note, int paymentAmount, int numberOfOrder) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setGuid(UUID.randomUUID());
        paymentEntity.setPaymentNote(note);
        paymentEntity.setPaymentAmount(paymentAmount);
        paymentEntity.setNumberOfOrder(numberOfOrder);
        paymentEntity.setPaymentMethod(PaymentMethod.CASH);
        paymentEntity.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        return paymentRepository.save(paymentEntity);
    }


}
