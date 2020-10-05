package vn.com.buaansach.entity.brand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.BannerType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_banner")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BannerEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "banner_name")
    private String bannerName;

    @Column(name = "banner_name_eng")
    private String bannerNameEng;

    @Column(name = "banner_description")
    private String bannerDescription;

    @Column(name = "banner_description_eng")
    private String bannerDescriptionEng;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "banner_type")
    private BannerType bannerType;

    @Column(name = "banner_order")
    private int bannerOrder;

    @Column(name = "banner_activated")
    private boolean bannerActivated;
}
