package acc.hotsix.file_share.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import acc.hotsix.file_share.domain.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long>, FileCustomRepository {
    List<File> findByNameAndPath(String name, String path);

    File findByLink(String link);
}
