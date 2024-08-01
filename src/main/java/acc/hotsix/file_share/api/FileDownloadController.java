package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDownloadService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileDownloadDto;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileDownloadController {

    private final FileService fileService;
    private final FileDownloadService fileDownloadService;

    // 파일 상세 조회 (메타데이터)
    @GetMapping("/{file-id}")
    public ResponseEntity<FileMetadataResponseDto> getFileMetadata(@PathVariable("file-id") Long fileId,
                                                                   @NotEmpty @RequestParam("password") String password) {
        try {
            fileService.validateFileAccess(fileId, password);
        } catch (Exception e) {
            // TODO 파일 접근 권한 없음 예외처리
        }
        FileMetadataResponseDto responseDto = fileService.getMetadataById(fileId);
        return ResponseEntity.ok().body(responseDto);
    }

    // 파일 다운로드
    @GetMapping("/{file-id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("file-id") Long fileId,
                                                 @NotEmpty @RequestParam("password") String password) throws IOException {
        try {
            fileService.validateFileAccess(fileId, password);
        } catch (Exception e) {
            // TODO 파일 접근 권한 없음 예외처리
        }
        FileDownloadDto downloadDto = fileDownloadService.downloadFile(fileId);

        byte[] content = downloadDto.getInputStream().readAllBytes();
        String filename = downloadDto.getFilename();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

}
