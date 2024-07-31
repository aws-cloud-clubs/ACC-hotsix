package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileDownloadController {

    private final FileService fileService;

    // 파일 상세 조회
    @GetMapping("/{file-id}")
    public ResponseEntity<FileMetadataResponseDto> getFileMetadata(@PathVariable("file-id") Long fileId) {
        FileMetadataResponseDto responseDto = fileService.getMetadataById(fileId);
        return ResponseEntity.ok().body(responseDto);
    }

}
