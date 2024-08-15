package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDownloadService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileDownloadDto;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileDownloadController {

    private final FileService fileService;
    private final FileDownloadService fileDownloadService;

    // 파일 다운로드
    @GetMapping("/download/{file-id}")
    public ResponseEntity downloadFile(@PathVariable("file-id") Long fileId,
                                       @NotEmpty @RequestParam("password") String password) throws Exception {
        ResponseEntity<HashMap> FORBIDDEN = isPasswordValid(fileId, password);
        if (FORBIDDEN != null) return FORBIDDEN;

        FileDownloadDto downloadDto = fileDownloadService.downloadFile(fileId);

        byte[] content = downloadDto.getByteArrayOutputStream().toByteArray();
        String filename = downloadDto.getFilename();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    private ResponseEntity<HashMap> isPasswordValid(Long fileId, String password) {
        try {
            if (!fileService.validateFileAccess(fileId, password)) {
                HashMap resultMap = new HashMap<>();
                resultMap.put("error", "Access denied: invalid password");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultMap);
            }
        } catch (FileNotFoundException e) {
            HashMap resultMap = new HashMap<>();
            resultMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
        return null;
    }
}
