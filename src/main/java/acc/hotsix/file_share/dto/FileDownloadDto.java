package acc.hotsix.file_share.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class FileDownloadDto {
    InputStream inputStream;
    String filename;
}
