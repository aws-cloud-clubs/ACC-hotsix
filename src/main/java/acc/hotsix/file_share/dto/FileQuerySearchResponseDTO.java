package acc.hotsix.file_share.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FileQuerySearchResponseDTO {
    private String name;
    private LocalDateTime createdAt;
    private String fileType;
    private String path;
}
