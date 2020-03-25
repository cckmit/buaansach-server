package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

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

    private UUID code;

    @Column(unique = true, length = 20)
    @Size(max = 20)
    private String customCode;

    @Column(length = 100)
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String address;

    @Column(name = "image_url")
    @Size(max = 255)
    private String imageUrl;

    @Column(name = "number_of_floor")
    private int numberOfFloor;

}
