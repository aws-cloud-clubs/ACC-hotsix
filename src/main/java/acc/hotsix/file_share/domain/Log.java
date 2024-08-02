package acc.hotsix.file_share.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="file_id")
    private File file;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum Type {
        CREATE, READ, UPDATE, DOWNLOAD
    }

    @Builder
    private Log(Type type, File file) {
        this.type = type;
        this.file = file;
    }
}

// file 생성(완), 조회(완), 업데이트(완), 다운로드(완) 시 Log 생성해야 함.
// log 조회 -> file id 별, type 별
