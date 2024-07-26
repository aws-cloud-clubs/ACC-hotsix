package acc.hotsix.file_share.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileQueryRequestDTO {
    private String nameSort;
    private String createdAtSort;
}
