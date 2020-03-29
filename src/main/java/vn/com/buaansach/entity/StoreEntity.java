package vn.com.buaansach.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "bas_store")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StoreEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(unique = true, length = 20)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String address;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean activated = true;

    @NotBlank
    @Size(max = 100)
    @Column(name = "owner_name", length = 100)
    private String ownerName;

    /* in case define multiple phone numbers */
    @NotBlank
    @Size(max = 50)
    @Column(name = "owner_phone")
    private String ownerPhone;

    @Email
    @Size(max = 255)
    @Column(name = "owner_email")
    private String ownerEmail;

    @Size(max = 100)
    @Column(name = "tax_code", length = 100)
    private String taxCode;

    @Size(max = 255)
    @Column(name = "update_reason")
    private String updateReason;

    @Column(name = "number_of_floors")
    private int numberOfFloors = 0;

    @Column(name = "number_of_seats")
    private int numberOfSeats = 0;
}
