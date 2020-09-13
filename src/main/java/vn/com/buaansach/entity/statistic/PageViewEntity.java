package vn.com.buaansach.entity.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bas_page_view")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PageViewEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(length = 10)
    private String location;

    @Column(name = "browser_name", length = 30)
    private String browserName;

    @Column(name = "browser_version", length = 10)
    private String browserVersion;

    @Column(name = "device_name", length = 30)
    private String deviceName;

    @Column(name = "device_os", length = 30)
    private String deviceOs;

    @Column(name = "device_os_version", length = 10)
    private String deviceOsVersion;
}
