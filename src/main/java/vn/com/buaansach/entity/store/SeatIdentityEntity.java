package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bas_seat_identity")
@Data
@NoArgsConstructor
public class SeatIdentityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true, name = "seat_guid")
    private UUID seatGuid;

    @Column(name = "user_agent")
    private String userAgent;

    private String platform;

    private String language;

    private String languages;

    @Column(name = "device_memory")
    private Integer deviceMemory;

    @Column(name = "hardware_concurrency")
    private Integer hardwareConcurrency;

    @Column(name = "screen_width")
    private Integer screenWidth; // window.screen.width

    @Column(name = "screen_height")
    private Integer screenHeight; // window.screen.height

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatIdentityEntity entity = (SeatIdentityEntity) o;
        return Objects.equals(seatGuid, entity.seatGuid) &&
                Objects.equals(userAgent, entity.userAgent) &&
                Objects.equals(platform, entity.platform) &&
                Objects.equals(language, entity.language) &&
                Objects.equals(languages, entity.languages) &&
                Objects.equals(deviceMemory, entity.deviceMemory) &&
                Objects.equals(hardwareConcurrency, entity.hardwareConcurrency) &&
                Objects.equals(screenWidth, entity.screenWidth) &&
                Objects.equals(screenHeight, entity.screenHeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatGuid, userAgent, platform, language, languages, deviceMemory, hardwareConcurrency, screenWidth, screenHeight);
    }
}
