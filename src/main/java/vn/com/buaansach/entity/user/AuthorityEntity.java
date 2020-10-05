package vn.com.buaansach.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "bas_authority")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 50)
    @Size(max = 50)
    private String name;
}
