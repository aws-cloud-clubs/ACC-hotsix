package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import acc.hotsix.file_share.dto.FileQueryRequestDTO;
import acc.hotsix.file_share.dto.FileQuerySearchResponseDTO;
import acc.hotsix.file_share.dto.FileSearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileQuerySearchService {
    private final FileRepository fileRepository;

    public List<FileQuerySearchResponseDTO> queryAllFile() {
        return this.fileRepository.sortAllFiles();
    }

    public Page<FileQuerySearchResponseDTO> queryFilesByPage (FileQueryRequestDTO fileQueryRequestDTO, Pageable pageable) {
        return this.fileRepository.sortFilesByNameAndDate(fileQueryRequestDTO, pageable);
    }

    public Page<FileQuerySearchResponseDTO> searchFilesByCriteria (FileSearchRequestDTO fileSearchRequestDTO, Pageable pageable) {
        return this.fileRepository.searchFilesByCriteria(fileSearchRequestDTO, pageable);
    }
}
