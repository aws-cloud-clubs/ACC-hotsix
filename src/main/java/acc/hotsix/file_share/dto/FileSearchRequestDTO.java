package acc.hotsix.file_share.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileSearchRequestDTO {
    private String name;
    private String path;
    private LocalDate before;
    private LocalDate after;
    private String fileType;
}
