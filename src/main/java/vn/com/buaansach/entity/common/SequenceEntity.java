package vn.com.buaansach.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "bas_sequence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "sequence_id")
    private String sequenceId;

    private int sequenceNumber;
}
