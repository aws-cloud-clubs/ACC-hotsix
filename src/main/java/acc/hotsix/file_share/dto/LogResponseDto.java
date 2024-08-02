package acc.hotsix.file_share.dto;

import acc.hotsix.file_share.domain.Log;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LogResponseDto {
    private Long logId;
    private Long fileId;
    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static LogResponseDto toLogResponseDto(Log log) {
        return LogResponseDto.builder()
                .logId(log.getLogId())
                .fileId(log.getFile().getFileId())
                .type(log.getType().toString())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
