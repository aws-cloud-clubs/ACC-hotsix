package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.global.error.exception.FileDuplicateException;
import acc.hotsix.file_share.global.error.exception.FileTypeMismatchException;
import acc.hotsix.file_share.global.error.exception.UpdateFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUpdateServiceImpl implements FileUpdateService {
    private final FileService fileService;
    private final FileUploadService fileUploadService;
    private final LogRepository logRepository;

    // 파일 업데이트
    public void updateFile(MultipartFile file, String fileId, String newDirectory) {
        File fileMetaData = fileService.getFileById(Long.parseLong(fileId));

        // 수정된 파일 S3에 업로드
        try {
            fileUploadService.uploadPresignedURL(Long.parseLong(fileId), file);
        } catch(Exception e) {
            throw new UpdateFileException();
        }

        fileMetaData.setName(file.getOriginalFilename());   // 메타 데이터 수정 - 파일명
        fileMetaData.setPath(newDirectory);                 // 메타 데이터 수정 - 경로
        fileMetaData.setLastModifiedAt(LocalDateTime.now());// 메타 데이터 수정 - 최종 수정 시간

        // 메타 데이터 수정 - 파일 사이즈
        double sizeInMegabytes = file.getSize() / (1024.0 * 1024.0);
        BigDecimal sizeRounded = new BigDecimal(sizeInMegabytes).setScale(2, RoundingMode.HALF_UP);
        fileMetaData.setFileSize(sizeRounded.doubleValue());

        // 업데이트된 메타 데이터 저장
        fileService.saveMetaData(fileMetaData);

        // 수정 로그 생성
        Log log = Log.builder()
                .type(Log.Type.UPDATE)
                .file(fileMetaData)
                .build();

        logRepository.save(log);
    }

}
