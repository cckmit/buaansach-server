package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosStoreOrderDTO extends AuditDTO {
    private UUID guid;
    private StoreOrderStatus storeOrderStatus;
    private StoreOrderType storeOrderType;
    private String firstSeenBy;
    private String firstHideBy;
    private boolean hideStoreOrder;
    private UUID storeGuid;
    private UUID areaGuid;
    private UUID seatGuid;
    private UUID orderGuid;
    private UUID orderProductGroup;
    private int numberOfProduct;

    public PosStoreOrderDTO() {
    }

    public PosStoreOrderDTO(StoreOrderEntity entity) {
        this.guid = entity.getGuid();
        this.storeOrderStatus = entity.getStoreOrderStatus();
        this.storeOrderType = entity.getStoreOrderType();
        this.firstSeenBy = entity.getFirstSeenBy();
        this.firstHideBy = entity.getFirstHideBy();
        this.hideStoreOrder = entity.isHideStoreOrder();
        this.storeGuid = entity.getStoreGuid();
        this.areaGuid = entity.getAreaGuid();
        this.seatGuid = entity.getSeatGuid();
        this.orderGuid = entity.getOrderGuid();
        this.orderProductGroup = entity.getOrderProductGroup();
        this.numberOfProduct = entity.getNumberOfProduct();

        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
