package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.application.FileShareService;
import acc.hotsix.file_share.dto.FileSharePostReq;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import acc.hotsix.file_share.global.error.InvalidShareLinkException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileShareController {
    private final FileService fileService;
    private final FileShareService fileShareService;

    Map<String, Object> resultMap;

    @PostMapping("/files/{file_id}/share")
    public ResponseEntity<Map<String, Object>> handleGenerateShareLink(@PathVariable(name="file_id") String fileId, @Valid @RequestBody FileSharePostReq req, BindingResult bindingResult) {
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

            String link = fileShareService.generateShareLink(Long.parseLong(fileId));
            resultMap.put("link", link);

            return ResponseEntity.ok(resultMap);
        } catch (FileNotFoundException e) {
            resultMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }

    }

    @GetMapping("/s/{share_link}")
    public ResponseEntity<Map<String, Object>> handleShareLink(@PathVariable(name="share_link") String shareLink) {
        resultMap = new HashMap<>();

        try {
            String resource = fileService.getResourceByLink(shareLink);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create(resource));

            System.out.println("controller resource "  + resource);

            return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
        } catch (InvalidShareLinkException e) {  
            resultMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }
}
