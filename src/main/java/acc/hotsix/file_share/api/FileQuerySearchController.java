package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileQuerySearchService;
import acc.hotsix.file_share.dto.FileQueryRequestDTO;
import acc.hotsix.file_share.dto.FileQuerySearchResponseDTO;
import acc.hotsix.file_share.dto.FileSearchRequestDTO;
import acc.hotsix.file_share.global.error.InvalidQueryReqParamException;
import acc.hotsix.file_share.global.error.MissingSearchReqParamException;
import acc.hotsix.file_share.global.error.NoQueryFilesException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileQuerySearchController {
    private final FileQuerySearchService fileQuerySearchService;

    // 전체 조회
    @GetMapping("/all")
    public List<FileQuerySearchResponseDTO> queryAllFiles() throws NoQueryFilesException {
        List<FileQuerySearchResponseDTO> content = this.fileQuerySearchService.queryAllFile();

        if(content.isEmpty()) {
            throw new NoQueryFilesException();
        }

        return content;
    }

    // 페이지 조회
    @GetMapping("")
    public List<FileQuerySearchResponseDTO> queryFilesByPage(
            @RequestParam(value = "name", required = false, defaultValue = "asc") String name,
            @RequestParam(value = "time", required = false, defaultValue = "asc") String time,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) throws NoQueryFilesException, InvalidQueryReqParamException {
        if((!name.isEmpty() && !name.equals("asc") && !name.equals("desc"))
        || (!time.isEmpty() && !time.equals("asc") && !time.equals("desc"))) {
            throw new InvalidQueryReqParamException();
        }

        FileQueryRequestDTO fileQueryRequestDTO = new FileQueryRequestDTO(name, time);
        Pageable pageable = PageRequest.of(page, 50);

        List<FileQuerySearchResponseDTO> content = this.fileQuerySearchService.queryFilesByPage(fileQueryRequestDTO, pageable).getContent();

        if(content.isEmpty()) {
            throw new NoQueryFilesException();
        }

        return content;
    }

    // 검색
    @GetMapping("/search")
    public List<FileQuerySearchResponseDTO> searchFilesByCriteria (
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "path", required = false) String path,
            @RequestParam(value = "before", required = false) LocalDate before,
            @RequestParam(value = "after", required = false) LocalDate after,
            @RequestParam(value = "type", required = false) String fileType,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) throws MissingSearchReqParamException, NoQueryFilesException {
        if(name == null && path == null && before == null && after == null && fileType == null) {
            throw new MissingSearchReqParamException();
        }

        FileSearchRequestDTO fileSearchRequestDTO = new FileSearchRequestDTO(name, path, before, after, fileType);
        Pageable pageable = PageRequest.of(page, 50);
        List<FileQuerySearchResponseDTO> content = this.fileQuerySearchService.searchFilesByCriteria(fileSearchRequestDTO, pageable).getContent();

        if(content.isEmpty()) {
            throw new NoQueryFilesException();
        }

        return content;
    }

    // 조회할 값이 없음
    @ExceptionHandler(NoQueryFilesException.class)
    public ResponseEntity<Map<String, Object>> noQueryFilesExceptionHandler(NoQueryFilesException e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
    }

    // 검색 조건이 없음
    @ExceptionHandler(MissingSearchReqParamException.class)
    public ResponseEntity<Map<String, Object>> missingSearchReqParamExceptionHandler(MissingSearchReqParamException e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

    // 파라미터가 asc, desc 가 아닌 다른 값임
    @ExceptionHandler(InvalidQueryReqParamException.class)
    public ResponseEntity<Map<String, Object>> invalidQueryReqParamExceptionHandler(InvalidQueryReqParamException e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

    // 파라미터 타입이 불일치
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> typeMismatchExceptionHandler (TypeMismatchException e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", "Invalid parameter data type");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

    // 서버 오류 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error", "An unexpected error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
    }
}
