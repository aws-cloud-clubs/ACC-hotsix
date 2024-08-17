package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import acc.hotsix.file_share.dao.LogRepository;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.domain.Log;
import acc.hotsix.file_share.dto.FileMetadataResponseDto;
import acc.hotsix.file_share.global.error.exception.FileNotFoundException;
import acc.hotsix.file_share.global.error.exception.ShareFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    private final PasswordEncoder passwordEncoder;

    private final LogRepository logRepository;

    // ID를 통한 파일 메타데이터 조회
    public File getFileById(Long fileId) throws FileNotFoundException {
        Optional<File> result = fileRepository.findById(fileId);

        if (!result.isPresent()) {
            throw new FileNotFoundException("File doesn't exit");
        }

        return result.get();
    }

    // 파일 메타 데이터 저장
    public File saveMetaData(File file) {
        return fileRepository.save(file);
    }

    // 파일 메타 데이터 삭제
    public void removeFileMetaData(File file) {
        fileRepository.delete(file);
    }

    // 이름과 경로가 동일한 파일 메타 데이터 리스트 조회
    public List<File> getSameNameAndPathFileList(String name, String directory) {
        return fileRepository.findByNameAndPath(name, directory);
    }

    // password 검증
    public boolean validateFileAccess(Long fileId, String password) throws FileNotFoundException {
        File file = getFileById(fileId);
        return passwordEncoder.matches(password, file.getPassword());
    }

    public HashMap getAllUrlMap() {
        List<File> fileList = fileRepository.findAll();
        HashMap<String, String> urlMap = new HashMap<>();

        for (File file : fileList) {
            urlMap.put(String.valueOf(file.getFileId()), file.getResource());
        }

        return urlMap;
    }

    public String getResourceByLink(String link) throws ShareFileException {
        File file = fileRepository.findByLink(link);
        if (file == null) {
            throw new ShareFileException();
        }
        return file.getResource();
    }

    // 파일 상세 조회
    @Transactional
    public FileMetadataResponseDto getMetadataById(Long fileId) {
        File file = fileRepository.findById(fileId).get();
        file.updateViewCount();

        Log log = Log.builder()
                .type(Log.Type.READ)
                .file(file)
                .build();

        logRepository.save(log);

        return FileMetadataResponseDto.toFileResponseDto(file);
    }
}
