package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.application.FileShareService;
import acc.hotsix.file_share.application.FileUpdateService;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileShareController {
    private final FileShareService fileShareService;

    Map<String, Object> resultMap;

    @PostMapping("/files/{file_id}/share")
    public ResponseEntity<Map<String, Object>> handleGenerateShareLink(@PathVariable(name="file_id") String fileId) {
        resultMap = new HashMap<>();
        try {
            String link = fileShareService.generateShareLink(Long.parseLong(fileId));
            resultMap.put("link", link);

            return ResponseEntity.ok(resultMap);
        } catch (Exception e) {
            return null;
        }
//        catch (FileNotFoundException e) {
//            resultMap.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
//        }

    }
}
