package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.enumeration.PaymentMethod;
import vn.com.buaansach.entity.enumeration.PaymentStatus;
import vn.com.buaansach.web.pos.repository.PosPaymentRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosPaymentService {
    private final PosPaymentRepository posPaymentRepository;

    public int calculatePayAmount(OrderEntity orderEntity){
        int payAmount = orderEntity.getOrderTotalAmount();
        if (orderEntity.getOrderDiscount() > 0) {
            switch (orderEntity.getOrderDiscountType()) {
                case VALUE:
                    payAmount = payAmount - orderEntity.getOrderDiscount();
                    break;
                case PERCENT:
                    payAmount = payAmount - (payAmount * orderEntity.getOrderDiscount() / 100);
                    break;
            }
        }
        /* Nếu payAmount < 0 thì vẫn set về 0 */
        payAmount = Math.max(payAmount, 0);
        return payAmount;
    }

    public PaymentEntity makeCashPayment(String note, OrderEntity orderEntity) {
        int paymentAmount = calculatePayAmount(orderEntity);
        return saveCashPayment(note, paymentAmount, 1);
    }

    public PaymentEntity makeCashPayment(String note, List<OrderEntity> listOrder) {
        int paymentAmount = 0;
        for (OrderEntity orderEntity: listOrder) {
            paymentAmount += calculatePayAmount(orderEntity);
        }
        return saveCashPayment(note, paymentAmount, listOrder.size());
    }

    public PaymentEntity saveCashPayment(String note, int paymentAmount, int numberOfOrder){
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setGuid(UUID.randomUUID());
        paymentEntity.setPaymentNote(note);
        paymentEntity.setPaymentAmount(paymentAmount);
        paymentEntity.setNumberOfOrder(numberOfOrder);
        paymentEntity.setPaymentMethod(PaymentMethod.CASH);
        paymentEntity.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        return posPaymentRepository.save(paymentEntity);
    }


}
