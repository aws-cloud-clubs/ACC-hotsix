package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.application.FileUpdateService;
import acc.hotsix.file_share.dto.UpdateFilePatchReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileUpdateController {
    private final FileUpdateService fileUpdateService;
    private final FileService fileService;

    Map<String, Object> resultMap;

    // 업데이트 핸들러
    @PatchMapping("/files/{file_id}")
    public ResponseEntity<Map<String, Object>> handleFileUpdate(
            @PathVariable("file_id") String fileId,
            @ModelAttribute UpdateFilePatchReq req
            ) {
        resultMap = new HashMap<>();

        MultipartFile file = req.getFile();
        String directory = req.getDirectory();
        String password = req.getPassword();

        if (!fileService.validateFileAccess(Long.parseLong(fileId), password)) {
            resultMap.put("error", "Access denied: invalid password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultMap);
        }

        try {
            fileUpdateService.updateFile(fileId, directory, file);
            resultMap.put("message", "File updated successfully");
            return ResponseEntity.ok(resultMap);
        } catch (Exception e) {     // TODO 에러 처리 추후 진행
            return null;
        }
//        catch (FileNotFoundException e) {
//            resultMap.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
//        } catch (UploadFileException e) {
//            resultMap.put("error", "File update failed: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
//        } catch (FileTypeMismatchException e) {
//            resultMap.put("error", "File type mismatch: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
//        } catch (Exception e) {
//            resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
//        }
    }
}
