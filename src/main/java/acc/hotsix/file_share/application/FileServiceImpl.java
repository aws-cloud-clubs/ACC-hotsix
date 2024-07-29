package acc.hotsix.file_share.application;

import acc.hotsix.file_share.dao.FileRepository;
import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

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

    // file_id와 password가 일치하는지 확인
    public boolean validateFileAccess(Long fileId, String password) {
        return (fileRepository.findByFileIdAndPassword(fileId, password) != null);
    }
}
