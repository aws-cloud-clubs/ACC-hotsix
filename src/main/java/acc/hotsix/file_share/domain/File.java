package acc.hotsix.file_share.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "file", indexes = {
        @Index(name = "idx_path", columnList = "path"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_file_type", columnList = "file_type")
})
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    private String resource;

    private String fileType;

    private Double fileSize;

    private String path;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @ColumnDefault("0")
    private Long download;

    @ColumnDefault("0")
    private Long view;

    private boolean uploaded;

    private String password;

    private String link;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Log> logs;

    @Builder
    public File(Long fileId, String name, LocalDateTime createdAt, String resource, String fileType, Double fileSize, String path, LocalDateTime lastModifiedAt, Long download, Long view, boolean uploaded, String password, String link) {
        this.fileId = fileId;
        this.name = name;
        this.createdAt = createdAt;
        this.resource = resource;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.path = path;
        this.lastModifiedAt = lastModifiedAt;
        this.download = download;
        this.view = view;
        this.uploaded = uploaded;
        this.password = password;
        this.link = link;
    }

    public void updateViewCount() {
        view = view + 1;
    }

    public void updateDownloadCount() {
        download = download + 1;
    }
}
