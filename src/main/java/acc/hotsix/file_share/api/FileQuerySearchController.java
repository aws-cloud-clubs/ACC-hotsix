package acc.hotsix.file_share.api;

import acc.hotsix.file_share.application.FileQuerySearchService;
import acc.hotsix.file_share.dto.FileQueryRequestDTO;
import acc.hotsix.file_share.dto.FileQuerySearchResponseDTO;
import acc.hotsix.file_share.dto.FileSearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileQuerySearchController {
    private final FileQuerySearchService fileQuerySearchService;

    @GetMapping("/all")
    public List<FileQuerySearchResponseDTO> queryAllFiles() {
        return this.fileQuerySearchService.queryAllFile();
    }

    @GetMapping("")
    public List<FileQuerySearchResponseDTO> queryFilesByPage(
            @RequestParam(value = "name", required = false, defaultValue = "asc") String name,
            @RequestParam(value = "time", required = false, defaultValue = "asc") String time,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) {
        FileQueryRequestDTO fileQueryRequestDTO = new FileQueryRequestDTO(name, time);
        Pageable pageable = PageRequest.of(page, 50);
        return this.fileQuerySearchService.queryFilesByPage(fileQueryRequestDTO, pageable).getContent();
    }

    @GetMapping("/search")
    public List<FileQuerySearchResponseDTO> searchFilesByCriteria(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "path", required = false) String path,
            @RequestParam(value = "before", required = false) LocalDate before,
            @RequestParam(value = "after", required = false) LocalDate after,
            @RequestParam(value = "type", required = false) String fileType,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) {
        FileSearchRequestDTO fileSearchRequestDTO = new FileSearchRequestDTO(name, path, before, after, fileType);
        Pageable pageable = PageRequest.of(page, 50);
        return this.fileQuerySearchService.searchFilesByCriteria(fileSearchRequestDTO, pageable).getContent();
    }

}
