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

    // 업로드 핸들러
    @PostMapping("/files")
    public ResponseEntity<Map<String, Object>> handleUploadFile(@Valid @ModelAttribute UploadFilePostReq req, BindingResult bindingResult) {
        resultMap = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                resultMap.put(error.getField(), error.getDefaultMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);   // TODO 에러 처리 추후 수정 예정
            }
        }

        MultipartFile file = req.getFile();
        String directory = req.getDirectory();
        String password = req.getPassword();

        try {
            fileUploadService.uploadFile(file, directory, password);
            resultMap.put("message", "File uploaded successfully");
            return ResponseEntity.ok(resultMap);
        } catch (Exception e) {     // TODO 에러 처리 추후 진행
            return null;
        }
//        catch (UploadFileException e) {
//            resultMap.put("error", "File upload failed: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
//        } catch (FileDuplicateException e) {
//            resultMap.put("error", "File Duplicated: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
//        } catch (Exception e) {
//            resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
//        }
    }
}