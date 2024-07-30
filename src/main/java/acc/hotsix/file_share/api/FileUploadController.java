package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileUploadService;
import acc.hotsix.file_share.dto.UploadFilePostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> handleUploadFile(@ModelAttribute UploadFilePostReq req) {
        resultMap = new HashMap<>();

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