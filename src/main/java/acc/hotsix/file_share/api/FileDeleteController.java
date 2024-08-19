package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDeleteService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.FilePasswordReq;
import acc.hotsix.file_share.global.error.exception.InvalidPasswordException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileDeleteController {
    private final FileService fileService;
    private final FileDeleteService fileDeleteService;

    @PostMapping("/files/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @PathVariable("id")Long fileId,
            @Valid @ModelAttribute FilePasswordReq req, BindingResult bindingResult
    ) {
        Map<String, Object> resultMap = new HashMap<>();

        String password = req.getPassword();
        if (!fileService.validateFileAccess(fileId, password)) {
            throw new InvalidPasswordException();
        }

        fileDeleteService.deleteFile(fileId);

        resultMap.put("message", "File deleted successfully");
        return ResponseEntity.ok(resultMap);
    }
}
