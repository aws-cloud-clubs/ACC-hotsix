package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileService;
import acc.hotsix.file_share.application.FileUpdateService;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.dto.UpdateFilePatchReq;
import acc.hotsix.file_share.global.error.exception.FileDuplicateException;
import acc.hotsix.file_share.global.error.exception.FileTypeMismatchException;
import acc.hotsix.file_share.global.error.exception.InvalidPasswordException;
import acc.hotsix.file_share.global.error.exception.UpdateFileException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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
            @Valid @ModelAttribute UpdateFilePatchReq req, BindingResult bindingResult
            ) {
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

        // 비밀번호를 이용한 파일 접근 권한 확인
        if (!fileService.validateFileAccess(Long.parseLong(fileId), password)) {
            throw new InvalidPasswordException();
        }

        File fileMetaData = fileService.getFileById(Long.parseLong(fileId));

        // 파일 타입 동일 여부 확인
        String fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileType == null) {
            fileType = file.getContentType();
        }
        if (!fileType.equals(fileMetaData.getFileType())) {
            throw new FileTypeMismatchException();
        }

        // 파일 중복 여부 확인
        List<File> duplicateFiles = fileService.getSameNameAndPathFileList(file.getOriginalFilename(), directory);
        for (File metaData : duplicateFiles) {
            if (!(metaData.getFileId() == Long.parseLong(fileId))) {
                throw new FileDuplicateException();
            }
        }

        // 파일 업데이트
        try {
            fileUpdateService.updateFile(file, fileId, directory);
            resultMap.put("message", "File updated successfully");
            return ResponseEntity.ok(resultMap);
        } catch (Exception e) {
            throw new UpdateFileException();
        }
    }
}
