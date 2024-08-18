package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileQuerySearchService;
import acc.hotsix.file_share.dto.FileQueryRequestDTO;
import acc.hotsix.file_share.dto.FileQuerySearchResponseDTO;
import acc.hotsix.file_share.dto.FileSearchRequestDTO;
import acc.hotsix.file_share.global.error.exception.InvalidQueryParamException;
import acc.hotsix.file_share.global.error.exception.MissingSearchParamException;
import acc.hotsix.file_share.global.error.exception.MissingSearchResultException;
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
    public List<FileQuerySearchResponseDTO> queryAllFiles() throws MissingSearchResultException {
        List<FileQuerySearchResponseDTO> content = this.fileQuerySearchService.queryAllFile();

        if(content.isEmpty()) {
            throw new MissingSearchResultException();
        }

        return content;
    }

    // 페이지 조회
    @GetMapping("")
    public List<FileQuerySearchResponseDTO> queryFilesByPage(
            @RequestParam(value = "name", required = false, defaultValue = "asc") String name,
            @RequestParam(value = "time", required = false, defaultValue = "asc") String time,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) throws MissingSearchResultException, InvalidQueryParamException {
        if((!name.isEmpty() && !name.equals("asc") && !name.equals("desc"))
        || (!time.isEmpty() && !time.equals("asc") && !time.equals("desc"))) {
            throw new InvalidQueryParamException();
        }

        FileQueryRequestDTO fileQueryRequestDTO = new FileQueryRequestDTO(name, time);
        Pageable pageable = PageRequest.of(page, 50);

        List<FileQuerySearchResponseDTO> content = this.fileQuerySearchService.queryFilesByPage(fileQueryRequestDTO, pageable).getContent();

        if(content.isEmpty()) {
            throw new MissingSearchResultException();
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
    ) throws MissingSearchParamException, MissingSearchResultException {
        if(name == null && path == null && before == null && after == null && fileType == null) {
            throw new MissingSearchParamException();
        }

        FileSearchRequestDTO fileSearchRequestDTO = new FileSearchRequestDTO(name, path, before, after, fileType);
        Pageable pageable = PageRequest.of(page, 50);
        List<FileQuerySearchResponseDTO> content = this.fileQuerySearchService.searchFilesByCriteria(fileSearchRequestDTO, pageable).getContent();

        if(content.isEmpty()) {
            throw new MissingSearchResultException();
        }

        return content;
    }

}
