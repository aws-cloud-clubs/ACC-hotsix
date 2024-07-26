package acc.hotsix.file_share.dao;

import acc.hotsix.file_share.dto.FileQueryRequestDTO;
import acc.hotsix.file_share.dto.FileQuerySearchResponseDTO;
import acc.hotsix.file_share.dto.FileSearchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface FileCustomRepository {
    List<FileQuerySearchResponseDTO> sortAllFiles ();
    Page<FileQuerySearchResponseDTO> sortFilesByNameAndDate(FileQueryRequestDTO fileQueryRequestDTO, Pageable pageable);
    Page<FileQuerySearchResponseDTO> searchFilesByCriteria(FileSearchRequestDTO fileSearchRequestDTO, Pageable pageable);
}
