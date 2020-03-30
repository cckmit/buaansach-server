package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_file")
@Data
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted=false")
public class FileEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID code;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(length = 10)
    private String extension;

    @Column(unique = true)
    @JsonIgnore
    private String localUrl;

    @Column(unique = true)
    private String url;

    private long size;

    @JsonIgnore
    private boolean deleted;
}
