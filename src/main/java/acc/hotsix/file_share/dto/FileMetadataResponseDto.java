package acc.hotsix.file_share.dto;

import acc.hotsix.file_share.domain.File;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileMetadataResponseDto {
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;
    private String resource;
    private String fileType;
    private String fileSize;
    private String path;
    private int downloadCount;
    private int viewCount;

    public static FileMetadataResponseDto toFileResponseDto(File file) {
        return FileMetadataResponseDto.builder()
                .id(file.getFileId())
                .name(file.getName())
                .createdAt(file.getCreatedAt())
                .lastModifiedAt(file.getLastModifiedAt())
                .resource(file.getResource())
                .fileType(file.getFileType())
                .fileSize(file.getFileSize().toString())
                .path(file.getPath())
                .downloadCount(file.getDownload().intValue())
                .viewCount(file.getView().intValue())
                .build();
    }
}
