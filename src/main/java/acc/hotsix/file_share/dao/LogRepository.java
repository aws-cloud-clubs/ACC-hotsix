package acc.hotsix.file_share.dao;

import acc.hotsix.file_share.domain.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByFileFileId(Long fileId, Pageable pageable);

    Page<Log> findByFileFileIdAndType(Long fileId, Log.Type type, Pageable pageable);
}
