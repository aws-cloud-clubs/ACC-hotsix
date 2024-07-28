package acc.hotsix.file_share.dao;

import acc.hotsix.file_share.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long>,  FileCustomRepository{
}
