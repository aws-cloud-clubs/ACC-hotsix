package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.global.error.FileDuplicateException;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import acc.hotsix.file_share.global.error.FileTypeMismatchException;
import acc.hotsix.file_share.global.error.UploadFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void updateFile(MultipartFile file, String fileId, String newDirectory)
            throws Exception {
        File fileMetaData = fileService.getFileById(Long.parseLong(fileId));

        // 파일 타입 동일 여부 확인
        String fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileType == null) {
            fileType = file.getContentType();
        }
        if (!fileType.equals(fileMetaData.getFileType())) {
            throw new FileTypeMismatchException(file.getOriginalFilename(), fileMetaData.getFileType(), fileType);
        }

        List<File> duplicateFiles = fileService.getSameNameAndPathFileList(file.getOriginalFilename(), newDirectory);
        for (File metaData : duplicateFiles) {
            if (!(metaData.getFileId() == Long.parseLong(fileId))) {    // 중복 파일 확인
                throw new FileDuplicateException("File duplicate.");
            }
        }

        // 수정된 파일 S3에 업로드
        fileUploadService.uploadPresignedURL(Long.parseLong(fileId), file);

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
