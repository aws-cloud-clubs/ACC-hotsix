package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDownloadService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FilePasswordReq;
import acc.hotsix.file_share.global.error.exception.InvalidPasswordException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileShareController {
    private final FileService fileService;

    private final FileDownloadService fileDownloadService;

    Map<String, Object> resultMap;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostMapping("/files/share/{file_id}")
    public ResponseEntity<Map<String, Object>> handleGenerateShareLink(@PathVariable(name="file_id") String fileId,
                                                                       @Valid @ModelAttribute FilePasswordReq req, BindingResult bindingResult) {
        resultMap = new HashMap<>();

        String password = req.getPassword();

        // 비밀번호를 이용한 파일 접근 권한 확인
        if (!fileService.validateFileAccess(Long.parseLong(fileId), password)) {
            throw new InvalidPasswordException();
        }

        // get presigned URL 발급
        String link = fileDownloadService.createPresignedGetUrl(bucketName, fileId);

        resultMap.put("link", link);

        return ResponseEntity.ok(resultMap);
    }
}
