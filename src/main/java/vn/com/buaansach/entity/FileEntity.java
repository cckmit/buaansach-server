package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_file")
@Data
@EqualsAndHashCode(callSuper = true)
public class FileEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 255)
    @Column(name = "original_name")
    private String originalName;

    @Size(max = 50)
    @Column(name = "content_type")
    private String contentType;

    @Column(length = 10)
    private String extension;

    @Size(max = 255)
    @JsonIgnore
    @Column(unique = true)
    private String localUrl;

    @Size(max = 255)
    @Column(unique = true)
    private String url;

    private long size;
}
