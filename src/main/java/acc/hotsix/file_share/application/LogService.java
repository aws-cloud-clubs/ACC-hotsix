package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.dto.LogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

//    public List<LogResponseDto> findLogsByFileId(Long fileId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        List<Log> content = logRepository.findByFileFileId(fileId, pageable).getContent();
//
//        return content.stream()
//                .map(log -> LogResponseDto.toLogResponseDto(log))
//                .collect(Collectors.toList());
//    }

    public List<LogResponseDto> findLogsByFileIdAndType(Long fileId, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Log> content;

        if(type.equals("none"))
            content = logRepository.findByFileFileId(fileId, pageable).getContent();
        else
            content = logRepository.findByFileFileIdAndType(fileId, Log.Type.valueOf(type), pageable).getContent();

        return content.stream()
                .map(log -> LogResponseDto.toLogResponseDto(log))
                .collect(Collectors.toList());
    }

}
