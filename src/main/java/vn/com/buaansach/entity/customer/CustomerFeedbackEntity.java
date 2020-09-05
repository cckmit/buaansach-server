package vn.com.buaansach.entity.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_customer_feedback")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFeedbackEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 50)
    @Column(name = "customer_name", length = 50)
    private String customerName;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Size(max = 20)
    @Column(name = "employee_code", length = 20)
    private String employeeCode;

    @Size(max = 100)
    @Column(name = "employee_name", length = 100)
    private String employeeName;


    @Size(max = 1000)
    @Column(name = "feedback_content", length = 1000)
    private String feedbackContent;
}
