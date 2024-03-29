package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {
    /**
     * Công thức tính giá tiền của hóa đơn. Cần đảm bảo giống với công thức trên UI
     * Giá tiền sẽ tính tất cả sản phẩm (kể cả chưa phục vụ), trừ sản phẩm đã bị hủy
     * Giá 1 Sản phẩm = Số lượng x (Giá bán - Giảm giá)
     */
    public int calculateTotalAmount(List<OrderProductEntity> orderProductEntities) {
        return orderProductEntities.stream()
                .filter(entity -> !entity.getOrderProductStatus().equals(OrderProductStatus.CANCELLED))
                .mapToInt(entity -> {
                    int price = entity.getOrderProductPrice();
                    if (entity.getOrderProductDiscount() > 0) {
                        switch (entity.getOrderProductDiscountType()) {
                            case VALUE:
                                price = price - entity.getOrderProductDiscount();
                                break;
                            case PERCENT:
                                price = price - (price * entity.getOrderProductDiscount() / 100);
                                break;
                        }
                    }
                    price = Math.max(price, 0);
                    return entity.getOrderProductQuantity() * price;
                }).sum();
    }

    public int calculatePayAmount(OrderEntity orderEntity) {
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

        payAmount = payAmount - orderEntity.getOrderPointValue();

        /* Nếu payAmount < 0 thì vẫn set về 0 */
        payAmount = Math.max(payAmount, 0);

        return payAmount;
    }
}
