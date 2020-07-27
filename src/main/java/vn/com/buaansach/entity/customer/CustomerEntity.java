package vn.com.buaansach.entity.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;
import vn.com.buaansach.entity.enumeration.Gender;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bas_customer")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 20)
    @Column(name = "customer_code", length = 20)
    private String customerCode;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Size(max = 100)
    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Size(max = 60)
    @Column(name = "customer_password_hash", length = 60)
    private String customerPassword;

    @Column(name = "customer_activated")
    private boolean customerActivated = true;

    @Size(max = 100)
    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_gender")
    private Gender customerGender;

    @Column(name = "customer_birthday")
    private Instant customerBirthday;

    @Size(max = 255)
    @Column(name = "customer_address")
    private String customerAddress;

    @Size(max = 255)
    @Column(name = "customer_image_url")
    private String customerImageUrl;

    @Size(max = 10)
    @Column(name = "customer_lang_key", length = 10)
    private String customerLangKey;

    @Size(max = 20)
    @Column(name = "customer_reset_key", length = 20)
    private String customerResetKey;

    @Column(name = "customer_reset_date")
    private Instant customerResetDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_zalo_status")
    private CustomerZaloStatus customerZaloStatus = CustomerZaloStatus.UNKNOWN;

    @Size(max = 255)
    @Column(name = "customer_zalo_id")
    private String customerZaloId;

    @Column(name = "customer_rank_guid")
    private UUID customerRankGuid;

    @Column(name = "store_guid")
    private UUID storeGuid;
}
