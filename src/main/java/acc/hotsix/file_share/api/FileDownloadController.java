package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDownloadService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileDownloadDto;
import acc.hotsix.file_share.global.error.exception.DownloadFileException;
import acc.hotsix.file_share.global.error.exception.InvalidPasswordException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileDownloadController {

    private final FileService fileService;
    private final FileDownloadService fileDownloadService;

    // 파일 다운로드
    @PostMapping("/download/{file-id}")
    public ResponseEntity downloadFile(@PathVariable("file-id") Long fileId,
                                       @Valid @ModelAttribute("password") String password) {
        if (!fileService.validateFileAccess(fileId, password)) {
            throw new InvalidPasswordException();
        }

        try {
            FileDownloadDto downloadDto = fileDownloadService.downloadFile(fileId);

            byte[] content = downloadDto.getByteArrayOutputStream().toByteArray();
            String filename = downloadDto.getFilename();
            String mimeType = Files.probeContentType(Paths.get(filename));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(mimeType));
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new DownloadFileException();
        }
    }
}
