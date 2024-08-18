package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileUploadService;
import acc.hotsix.file_share.dto.UploadFilePostReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    Map<String, Object> resultMap;

    // 업로드 presignedURL 요청 및 처리 핸들러
    @PostMapping("/files")
    public ResponseEntity<Map<String, Object>> handleUploadFile(@Valid @ModelAttribute UploadFilePostReq req,
                                                                BindingResult bindingResult) {
        resultMap = new HashMap<>();

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                resultMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        MultipartFile file = req.getFile();
        String directory = req.getDirectory();
        String password = req.getPassword();

        fileUploadService.uploadFileToS3(file, directory, password);
        resultMap.put("message", "File uploaded successfully");

        return ResponseEntity.ok(resultMap);
    }
}