package acc.hotsix.file_share.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder

    public File(Long fileId, String name, LocalDateTime createdAt, String resource, String fileType, Double fileSize, String path, LocalDateTime lastModifiedAt, Long download, Long view) {
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
    }
}
