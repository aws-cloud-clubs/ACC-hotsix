package acc.hotsix.file_share.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileQuerySearchResponseDTO {
    private String name;
    private LocalDateTime createdAt;
    private String fileType;
    private String path;
}
