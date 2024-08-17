package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDownloadService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FileSharePostReq;
import acc.hotsix.file_share.global.error.exception.FileNotFoundException;
import acc.hotsix.file_share.global.error.exception.ShareFileException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public ResponseEntity<Map<String, Object>> handleGenerateShareLink(@PathVariable(name="file_id") String fileId, @Valid @ModelAttribute FileSharePostReq req, BindingResult bindingResult) {
        resultMap = new HashMap<>();

        String password = req.getPassword();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                resultMap.put(error.getField(), error.getDefaultMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);   // TODO 에러 처리 추후 수정 예정
            }
        }

        try {
            // 비밀번호를 이용한 파일 접근 권한 확인
            if (!fileService.validateFileAccess(Long.parseLong(fileId), password)) {
                resultMap.put("error", "Access denied: invalid password");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultMap);
            }

            // get presigned URL 발급
            String link = fileDownloadService.createPresignedGetUrl(bucketName, fileId);

            resultMap.put("link", link);

            return ResponseEntity.ok(resultMap);
        } catch (FileNotFoundException e) {
            resultMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        } catch (ShareFileException e) {
            resultMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }

    }
}
