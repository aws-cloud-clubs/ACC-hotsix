package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDownloadService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import acc.hotsix.file_share.dto.FilePasswordReq;
import acc.hotsix.file_share.global.error.exception.InvalidPasswordException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileDetailController {

    private final FileService fileService;

    private final FileDownloadService fileDownloadService;

    Map<String, Object> resultMap;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 상세 조회 (메타데이터)
    @PostMapping("/detail-meta/{file-id}")
    public ResponseEntity getFileMetadata(@PathVariable("file-id") Long fileId,
                                          @Valid @ModelAttribute FilePasswordReq req, BindingResult bindingResult) {
        // 비밀번호를 이용한 파일 접근 권한 확인
        String password = req.getPassword();
        if (!fileService.validateFileAccess(fileId, password)) {
            throw new InvalidPasswordException();
        }

        FileMetadataResponseDto responseDto = fileService.getMetadataById(fileId);
        return ResponseEntity.ok().body(responseDto);
    }

    // 파일 상세 조회(파일)
    @PostMapping("/detail-file/{file_id}")
    public ResponseEntity<Map<String, Object>> handleGenerateShareLink(@PathVariable(name="file_id") String fileId,
                                                                       @Valid @ModelAttribute FilePasswordReq req, BindingResult bindingResult) {
        resultMap = new HashMap<>();

        // 비밀번호를 이용한 파일 접근 권한 확인
        String password = req.getPassword();
        if (!fileService.validateFileAccess(Long.parseLong(fileId), password)) {
            throw new InvalidPasswordException();
        }

        // get presigned URL 발급
        String link = fileDownloadService.createPresignedGetUrl(bucketName, fileId);

        // 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(link));

        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }
}
