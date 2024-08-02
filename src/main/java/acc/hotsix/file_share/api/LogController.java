package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.LogService;
import acc.hotsix.file_share.dto.LogResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class LogController {

    private final LogService logService;

//    @GetMapping("/{file-id}/logs")
//    public ResponseEntity<List<LogResponseDto>> getFileLogs(@PathVariable("file-id") Long fileId,
//                                                            @RequestParam(defaultValue = "0") int page,
//                                                            @RequestParam(defaultValue = "10") int size) {
//
//        return ResponseEntity.ok().body(logService.findLogsByFileId(fileId, page, size));
//    }

    @GetMapping("/{file-id}/logs")
    public ResponseEntity<List<LogResponseDto>> getFileLogsByType(@PathVariable("file-id") Long fileId,
                                                            @RequestParam(required = false,defaultValue = "none") String type,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok().body(logService.findLogsByFileIdAndType(fileId, type, page, size));
    }
}

// 로그 파일id로 조회,
// 로그 타입으로 조회, 로그 생성 시각으로 조회