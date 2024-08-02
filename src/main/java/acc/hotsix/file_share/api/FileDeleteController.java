package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileDeleteService;
import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.dto.DeleteFileReq;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileDeleteController {
    private final FileService fileService;
    private final FileDeleteService fileDeleteService;

    @PostMapping("/files/{id}/delete")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @PathVariable("id")Long fileId,
            @Valid @ModelAttribute DeleteFileReq req, BindingResult bindingResult
    ) {
        Map<String, Object> resultMap = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                resultMap.put(error.getField(), error.getDefaultMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);   // TODO 에러 처리 추후 수정 예정
            }
        }

        String password = req.getPassword();
        try {
            if (!fileService.validateFileAccess(fileId, password)) {
                resultMap.put("error", "Access denied: invalid password");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultMap);
            }
        } catch (FileNotFoundException e) {
            resultMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        fileDeleteService.deleteFile(fileId);

        resultMap.put("message", "File deleted successfully");
        return ResponseEntity.ok(resultMap);
    }

    // 파라미터 타입이 불일치
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> typeMismatchExceptionHandler (TypeMismatchException e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", "Invalid parameter data type");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
    }

}
