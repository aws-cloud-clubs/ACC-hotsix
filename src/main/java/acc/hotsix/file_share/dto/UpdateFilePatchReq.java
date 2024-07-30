package acc.hotsix.file_share.dto;

import acc.hotsix.file_share.dto.validation.DirectoryPath;
import acc.hotsix.file_share.dto.validation.FileNotEmpty;
import acc.hotsix.file_share.dto.validation.ValidFileType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFilePatchReq {
    @NotNull
    @FileNotEmpty
    @ValidFileType
    private MultipartFile file;

    @NotNull
    @NotEmpty
    @DirectoryPath
    private String directory;

    @NotNull
    @NotEmpty
    private String password;
}
