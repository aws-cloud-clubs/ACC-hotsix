package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileDownloadController {

    private final FileService fileService;

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


}
