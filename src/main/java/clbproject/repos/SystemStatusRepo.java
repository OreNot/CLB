package clbproject.repos;

import clbproject.domain.SystemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemStatusRepo extends JpaRepository<SystemStatus, Integer> {

    List<SystemStatus> findAll();
    Optional<SystemStatus> findById(Integer id);
    SystemStatus findByName(String name);

}
