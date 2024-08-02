package acc.hotsix.file_share.dao;

import acc.hotsix.file_share.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

}
